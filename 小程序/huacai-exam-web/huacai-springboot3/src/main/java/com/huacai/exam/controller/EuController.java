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
import com.huacai.exam.domain.Eu;
import com.huacai.exam.service.IEuService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 考试用户分配Controller
 *
 * @author huacai
 * @date 2025-09-29
 */
@RestController
@RequestMapping("/exam/eu")
public class EuController extends BaseController {
    @Autowired
    private IEuService euService;

    /**
     * 查询考试用户分配列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Eu eu) {
        startPage();
        List<Eu> list = euService.selectEuList(eu);
        return getDataTable(list);
    }

    /**
     * 导出考试用户分配列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Eu eu) {
        List<Eu> list = euService.selectEuList(eu);
        ExcelUtil<Eu> util = new ExcelUtil<Eu>(Eu. class);
        util.exportExcel(response, list, "考试用户分配数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Eu> util = new ExcelUtil<Eu>(Eu. class);
        util.importTemplateExcel(response, "考试用户分配数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Eu> util = new ExcelUtil<Eu>(Eu. class);
        InputStream inputStream = file.getInputStream();
        List<Eu> list = util.importExcel(inputStream);
        inputStream.close();
        int count = euService.batchInsertEu(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取考试用户分配详细信息
     */
    @GetMapping(value = "/{euId}")
    public AjaxResult getInfo(@PathVariable("euId") String euId) {
        return success(euService.selectEuByEuId(euId));
    }

    /**
     * 新增考试用户分配
     */
    @PostMapping
    public AjaxResult add(@RequestBody Eu eu) {
        return toAjax(euService.insertEu(eu));
    }

    /**
     * 修改考试用户分配
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Eu eu) {
        return toAjax(euService.updateEu(eu));
    }

    /**
     * 删除考试用户分配
     */
    @DeleteMapping("/{euIds}")
    public AjaxResult remove(@PathVariable String[] euIds) {
        return toAjax(euService.deleteEuByEuIds(euIds));
    }

    /**
     * 查询个人的所有考试
     */
    @GetMapping("/selectMyExam")
    public AjaxResult selectMyExam() {
        Eu eu = new Eu();
        eu.setUserId(getUserId());
        List<Eu> list = euService.selectEuList(eu);
        return success(list);
    }

    /**
     * 查询个人已考完的考试列表
     */
    @GetMapping("/selectMyExamEndRecord")
    public AjaxResult selectMyExamEndRecord() {
        Long userId = getUserId();
        return success(euService.selectMyExamEndRecord(userId));
    }

    /**
     * 查询个人的考试次数和通过次数
     */
    @GetMapping("/selectMyExamCountAndPassCount")
    public AjaxResult selectMyExamCountAndPassCount() {
        Long userId = getUserId();
        return success(euService.selectMyExamCountAndPassCount(userId));
    }

}
