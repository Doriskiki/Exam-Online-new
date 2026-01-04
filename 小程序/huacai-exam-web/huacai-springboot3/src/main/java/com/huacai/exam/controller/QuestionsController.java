package com.huacai.exam.controller;

import java.util.List;

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
import com.huacai.exam.domain.Questions;
import com.huacai.exam.service.IQuestionsService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 题目Controller
 *
 * @author huacai
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/exam/questions")
public class QuestionsController extends BaseController {
    @Autowired
    private IQuestionsService questionsService;

    /**
     * 查询题目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Questions questions) {
        startPage();
        List<Questions> list = questionsService.selectQuestionsList(questions);
        return getDataTable(list);
    }

    /**
     * 导出题目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Questions questions) {
        List<Questions> list = questionsService.selectQuestionsList(questions);
        ExcelUtil<Questions> util = new ExcelUtil<Questions>(Questions. class);
        util.exportExcel(response, list, "题目数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Questions> util = new ExcelUtil<Questions>(Questions. class);
        util.importTemplateExcel(response, "题目数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Questions> util = new ExcelUtil<Questions>(Questions. class);
        InputStream inputStream = file.getInputStream();
        List<Questions> list = util.importExcel(inputStream);
        inputStream.close();
        int count = questionsService.batchInsertQuestions(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取题目详细信息
     */
    @GetMapping(value = "/{questionId}")
    public AjaxResult getInfo(@PathVariable("questionId") String questionId) {
        return success(questionsService.selectQuestionsByQuestionId(questionId));
    }

    /**
     * 新增题目
     */
    @PostMapping
    public AjaxResult add(@RequestBody Questions questions) {
        return toAjax(questionsService.insertQuestions(questions));
    }

    /**
     * 修改题目
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Questions questions) {
        return toAjax(questionsService.updateQuestions(questions));
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/{questionIds}")
    public AjaxResult remove(@PathVariable String[] questionIds) {
        return toAjax(questionsService.deleteQuestionsByQuestionIds(questionIds));
    }

    /**
     * 查询对应分类的所有题目
     */
    @GetMapping("/selectAllListByCategoryId/{categoryId}")
    public AjaxResult selectAllListByCategoryId(@PathVariable("categoryId") String categoryId) {
        Questions questions = new Questions();
        questions.setCategoryId(categoryId);
        List<Questions> list = questionsService.selectQuestionsList(questions);
        return success(list);
    }
}
