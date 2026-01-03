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
import com.huacai.exam.domain.Eq;
import com.huacai.exam.service.IEqService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 试题Controller
 *
 * @author huacai
 * @date 2025-09-27
 */
@RestController
@RequestMapping("/exam/eq")
public class EqController extends BaseController {
    @Autowired
    private IEqService eqService;

    /**
     * 查询试题列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Eq eq) {
        startPage();
        List<Eq> list = eqService.selectEqList(eq);
        return getDataTable(list);
    }

    /**
     * 导出试题列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Eq eq) {
        List<Eq> list = eqService.selectEqList(eq);
        ExcelUtil<Eq> util = new ExcelUtil<Eq>(Eq. class);
        util.exportExcel(response, list, "试题数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Eq> util = new ExcelUtil<Eq>(Eq. class);
        util.importTemplateExcel(response, "试题数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Eq> util = new ExcelUtil<Eq>(Eq. class);
        InputStream inputStream = file.getInputStream();
        List<Eq> list = util.importExcel(inputStream);
        inputStream.close();
        int count = eqService.batchInsertEq(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取试题详细信息
     */
    @GetMapping(value = "/{eqId}")
    public AjaxResult getInfo(@PathVariable("eqId") String eqId) {
        return success(eqService.selectEqByEqId(eqId));
    }

    /**
     * 新增试题
     */
    @PostMapping
    public AjaxResult add(@RequestBody Eq eq) {
        return toAjax(eqService.insertEq(eq));
    }

    /**
     * 修改试题
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Eq eq) {
        return toAjax(eqService.updateEq(eq));
    }

    /**
     * 删除试题
     */
    @DeleteMapping("/{eqIds}")
    public AjaxResult remove(@PathVariable String[] eqIds) {
        return toAjax(eqService.deleteEqByEqIds(eqIds));
    }

    /**
     * 根据考试ID查询对应的试题列表
     */
    @GetMapping("/selectExamQuestionsListByExamId/{examId}")
    public AjaxResult selectExamQuestionsListByExamId(@PathVariable String examId) {
        return success(eqService.selectExamQuestionsListByExamId(examId));
    }

}
