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
import com.huacai.exam.domain.Guide;
import com.huacai.exam.service.IGuideService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 学习指南Controller
 *
 * @author huacai
 * @date 2025-10-02
 */
@RestController
@RequestMapping("/exam/guide")
public class GuideController extends BaseController {
    @Autowired
    private IGuideService guideService;

    /**
     * 查询学习指南列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Guide guide) {
        startPage();
        List<Guide> list = guideService.selectGuideList(guide);
        return getDataTable(list);
    }

    /**
     * 导出学习指南列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Guide guide) {
        List<Guide> list = guideService.selectGuideList(guide);
        ExcelUtil<Guide> util = new ExcelUtil<Guide>(Guide. class);
        util.exportExcel(response, list, "学习指南数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Guide> util = new ExcelUtil<Guide>(Guide. class);
        util.importTemplateExcel(response, "学习指南数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Guide> util = new ExcelUtil<Guide>(Guide. class);
        InputStream inputStream = file.getInputStream();
        List<Guide> list = util.importExcel(inputStream);
        inputStream.close();
        int count = guideService.batchInsertGuide(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取学习指南详细信息
     */
    @GetMapping(value = "/{guideId}")
    public AjaxResult getInfo(@PathVariable("guideId") String guideId) {
        return success(guideService.selectGuideByGuideId(guideId));
    }

    /**
     * 新增学习指南
     */
    @PostMapping
    public AjaxResult add(@RequestBody Guide guide) {
        return toAjax(guideService.insertGuide(guide));
    }

    /**
     * 修改学习指南
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Guide guide) {
        return toAjax(guideService.updateGuide(guide));
    }

    /**
     * 删除学习指南
     */
    @DeleteMapping("/{guideIds}")
    public AjaxResult remove(@PathVariable String[] guideIds) {
        return toAjax(guideService.deleteGuideByGuideIds(guideIds));
    }

    /**
     * 查询所有学习指南
     */
    @GetMapping("/selectAllGuide")
    public AjaxResult selectAllGuide() {
        Guide guide = new Guide();
        List<Guide> list = guideService.selectGuideList(guide);
        return success(list);
    }

}
