package com.haoran.Brainstorming.controller.admin;

import com.haoran.Brainstorming.service.ISystemConfigService;
import com.haoran.Brainstorming.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/admin/system")
public class SystemConfigAdminController extends BaseAdminController {

    @Resource
    private ISystemConfigService systemConfigService;

    @RequiresPermissions("system:edit")
    @GetMapping("/edit")
    public String edit(Model model) {
        model.addAttribute("systems", systemConfigService.selectAll());
        return "admin/system/edit";
    }

    @RequiresPermissions("system:edit")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(@RequestBody List<Map<String, String>> list) {
        systemConfigService.update(list);
        return success();
    }
}
