package com.huacai.exam.controller;

import java.util.List;

import com.huacai.exam.domain.Eq;
import com.huacai.exam.service.IEqService;
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
import com.huacai.exam.domain.Exam;
import com.huacai.exam.service.IExamService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 考试Controller
 *
 * @author huacai
 * @date 2025-09-27
 */
@RestController
@RequestMapping("/exam/exam")
public class ExamController extends BaseController {
    @Autowired
    private IExamService examService;

    @Autowired
    private IEqService eqService;

    /**
     * 查询考试列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Exam exam) {
        startPage();
        List<Exam> list = examService.selectExamList(exam);
        return getDataTable(list);
    }

    /**
     * 导出考试列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Exam exam) {
        List<Exam> list = examService.selectExamList(exam);
        ExcelUtil<Exam> util = new ExcelUtil<Exam>(Exam. class);
        util.exportExcel(response, list, "考试数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Exam> util = new ExcelUtil<Exam>(Exam. class);
        util.importTemplateExcel(response, "考试数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Exam> util = new ExcelUtil<Exam>(Exam. class);
        InputStream inputStream = file.getInputStream();
        List<Exam> list = util.importExcel(inputStream);
        inputStream.close();
        int count = examService.batchInsertExam(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取考试详细信息
     */
    @GetMapping(value = "/{examId}")
    public AjaxResult getInfo(@PathVariable("examId") String examId) {
        return success(examService.selectExamByExamId(examId));
    }

    /**
     * 新增考试
     */
    @PostMapping
    public AjaxResult add(@RequestBody Exam exam) {
        return toAjax(examService.insertExam(exam));
    }

    /**
     * 修改考试
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Exam exam) {
        return toAjax(examService.updateExam(exam));
    }

    /**
     * 删除考试
     */
    @DeleteMapping("/{examIds}")
    public AjaxResult remove(@PathVariable String[] examIds) {
        return toAjax(examService.deleteExamByExamIds(examIds));
    }

    /**
     * 根据考试ID删除试题
     */
    @DeleteMapping("/deleteEqByExamId/{examId}")
    public AjaxResult deleteEqByExamId(@PathVariable String examId) {
        //查询试题表中是否存在该考试ID
        Eq eq = new Eq();
        eq.setExamId(examId);
        List<Eq> eqList = eqService.selectEqList(eq);
        if (eqList.size() == 0) {
            return success();
        } else {
           int result = eqService.deleteEqByExamId(examId);
           return toAjax(result);
        }
    }

    /**
     * 自动组卷
     */
    @PostMapping("/autoCompose/{examId}")
    public AjaxResult autoCompose(@PathVariable String examId) {
        return success(examService.autoCompose(examId));
    }

}
