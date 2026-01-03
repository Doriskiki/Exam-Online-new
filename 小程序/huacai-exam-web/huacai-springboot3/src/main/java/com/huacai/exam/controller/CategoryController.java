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
import com.huacai.exam.domain.Category;
import com.huacai.exam.service.ICategoryService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 题目分类Controller
 *
 * @author huacai
 * @date 2025-09-25
 */
@RestController
@RequestMapping("/exam/category")
public class CategoryController extends BaseController {
    @Autowired
    private ICategoryService categoryService;

    /**
     * 查询题目分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Category category) {
        startPage();
        List<Category> list = categoryService.selectCategoryList(category);
        return getDataTable(list);
    }

    /**
     * 导出题目分类列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Category category) {
        List<Category> list = categoryService.selectCategoryList(category);
        ExcelUtil<Category> util = new ExcelUtil<Category>(Category. class);
        util.exportExcel(response, list, "题目分类数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Category> util = new ExcelUtil<Category>(Category. class);
        util.importTemplateExcel(response, "题目分类数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Category> util = new ExcelUtil<Category>(Category. class);
        InputStream inputStream = file.getInputStream();
        List<Category> list = util.importExcel(inputStream);
        inputStream.close();
        int count = categoryService.batchInsertCategory(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取题目分类详细信息
     */
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId) {
        return success(categoryService.selectCategoryByCategoryId(categoryId));
    }

    /**
     * 新增题目分类
     */
    @PostMapping
    public AjaxResult add(@RequestBody Category category) {
        return toAjax(categoryService.insertCategory(category));
    }

    /**
     * 修改题目分类
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Category category) {
        return toAjax(categoryService.updateCategory(category));
    }

    /**
     * 删除题目分类
     */
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds) {
        return toAjax(categoryService.deleteCategoryByCategoryIds(categoryIds));
    }

    /**
     * 查询所有分类列表数据
     */
    @GetMapping("/selectAllCategoryList")
    public AjaxResult selectAllCategoryList() {
        Category category = new Category();
        List<Category> list = categoryService.selectCategoryList(category);
        return AjaxResult.success(list);
    }

    /**
     * 获取题目类型分布数据
     */
    @GetMapping("/getQuestionTypeStats")
    public AjaxResult getQuestionTypeStats() {
        List<Category> list = categoryService.getQuestionTypeStats();
        return AjaxResult.success(list);
    }

}
