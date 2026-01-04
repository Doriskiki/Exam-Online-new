package com.huacai.exam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huacai.exam.domain.Eq;
import com.huacai.exam.domain.Eu;
import com.huacai.exam.domain.Questions;
import com.huacai.exam.domain.dto.CompleteExamDTO;
import com.huacai.exam.service.*;
import com.huacai.system.general.utils.uuid.IdUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import com.huacai.exam.domain.Er;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 考试结果Controller
 *
 * @author huacai
 * @date 2025-09-30
 */
@RestController
@RequestMapping("/exam/er")
public class ErController extends BaseController {
    @Autowired
    private IErService erService;

    @Autowired
    private IQuestionsService questionsService;

    @Autowired
    private IEqService eqService;

    @Autowired
    private IEuService euService;

    @Autowired
    private IExamService examService;

    /**
     * 查询考试结果列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Er er) {
        startPage();
        List<Er> list = erService.selectErList(er);
        return getDataTable(list);
    }

    /**
     * 导出考试结果列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Er er) {
        List<Er> list = erService.selectErList(er);
        ExcelUtil<Er> util = new ExcelUtil<Er>(Er. class);
        util.exportExcel(response, list, "考试结果数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Er> util = new ExcelUtil<Er>(Er. class);
        util.importTemplateExcel(response, "考试结果数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Er> util = new ExcelUtil<Er>(Er. class);
        InputStream inputStream = file.getInputStream();
        List<Er> list = util.importExcel(inputStream);
        inputStream.close();
        int count = erService.batchInsertEr(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取考试结果详细信息
     */
    @GetMapping(value = "/{erId}")
    public AjaxResult getInfo(@PathVariable("erId") String erId) {
        return success(erService.selectErByErId(erId));
    }

    /**
     * 新增考试结果
     */
    @PostMapping
    public AjaxResult add(@RequestBody Er er) {
        return toAjax(erService.insertEr(er));
    }

    /**
     * 修改考试结果
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Er er) {
        return toAjax(erService.updateEr(er));
    }

    /**
     * 删除考试结果
     */
    @DeleteMapping("/{erIds}")
    public AjaxResult remove(@PathVariable String[] erIds) {
        return toAjax(erService.deleteErByErIds(erIds));
    }

    /**
     * 完成考试
     */
    @PostMapping("/completeExam")
    public AjaxResult completeExam(@RequestBody List<CompleteExamDTO> completeExamDTO) {
        //统计正确题数
        int correctCount = 0;
        //得分
        int totalScore = 0;

        List<Er> examResults = new ArrayList<>();

        //拿到每一个答题结果
        for (CompleteExamDTO examDTO : completeExamDTO) {
            //创建考试结果对象
            Er er = new Er();
            er.setErId(IdUtils.fastSimpleUUID());
            er.setExamId(examDTO.getExamId());
            er.setQuestionId(examDTO.getQuestionId());
            er.setUserAnswer(examDTO.getUserAnswer());

            //查询题目的正确答案
            Questions question = questionsService.selectQuestionsByQuestionId(examDTO.getQuestionId());
            if (question != null) {
                er.setCorrectAnswer(question.getAnswer());

                //判断答案是否正确
                if (question.getAnswer().equals(examDTO.getUserAnswer())) {
                    er.setIsCorrect("正确");
                    correctCount++;
                } else {
                    er.setIsCorrect("错误");
                }
            }

            //查询题目分数
            Eq eqQuery = new Eq();
            eqQuery.setExamId(examDTO.getExamId());
            eqQuery.setQuestionId(examDTO.getQuestionId());
            List<Eq> eqList = eqService.selectEqList(eqQuery);
            if (eqList != null && !eqList.isEmpty()) {
                Eq eq = eqList.get(0);
                Long questionScore = eq.getScore();
                //如果答对了, 就加上这道题的分数
                if ("正确".equals(er.getIsCorrect())) {
                    totalScore += questionScore;
                }
            }
            //获取当前用户ID
            er.setUserId(getUserId());
            examResults.add(er);
        }

        //批量保存考试结果
        if (!examResults.isEmpty()) {
            erService.batchInsertEr(examResults);
        }

        //返回包括正确题数和总分的结果
        HashMap<String, Object> result = new HashMap<>();
        result.put("correctCount", correctCount);
        result.put("totalScore", totalScore);


        //为考试用户分配表赋值

        //获取考试ID
        String examId = completeExamDTO.get(0).getExamId();

        //获取当前用户ID
        Long userId = getUserId();

        //根据考试ID和用户ID查询考试用户分配ID
        String euId = euService.selectEuIdByExamIdAndUserId(examId, userId);

        //查询该考试的及格分
        Long passScore = examService.selectExamByExamId(examId).getPassScore();

        //修改对应的考试用户分配表
        Eu eu = new Eu();
        eu.setEuId(euId);
        eu.setCorrectCount((long) correctCount);
        eu.setScore((long) totalScore);
        if (totalScore < passScore) {
            eu.setStatus("考试未通过");
        } else {
            eu.setStatus("考试通过");
        }
        euService.updateEu(eu);

        return success(euId);
    }

    /**
     * 根据考试ID和用户ID查询考试结果列表
     */
    @GetMapping("/selectErListByExamIdAndUserId/{examId}")
    public AjaxResult selectErListByExamIdAndUserId(@PathVariable String examId) {
        Long userId = getUserId();
        return success(erService.selectErListByExamIdAndUserId(examId, userId));
    }

}
