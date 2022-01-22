package com.haoran.Brainstorming.controller.admin;

import com.haoran.Brainstorming.model.AdminUser;
import com.haoran.Brainstorming.service.IAdminUserService;
import com.haoran.Brainstorming.service.IPermissionService;
import com.haoran.Brainstorming.service.IRoleService;
import com.haoran.Brainstorming.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/role")
public class RoleAdminController extends BaseAdminController {

    @Resource
    private IRoleService roleService;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private IAdminUserService adminUserService;

    @RequiresPermissions("role:list")
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("roles", roleService.selectAll());
        return "admin/role/list";
    }

    @RequiresPermissions("role:add")
    @GetMapping("/add")
    public String add(Model model) {
        // Interroger toutes les autorisations
        model.addAttribute("permissions", permissionService.selectAll());
        return "admin/role/add";
    }

    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public String save(String name, Integer[] permissionIds) {
        roleService.insert(name, permissionIds);
        return redirect("/admin/role/list");
    }

    @RequiresPermissions("role:edit")
    @GetMapping("/edit")
    public String edit(Integer id, Model model) {
        model.addAttribute("role", roleService.selectById(id));
        // Interroge les permissions associées au rôle actuel
        model.addAttribute("currentPermissions", permissionService.selectByRoleId(id));
        model.addAttribute("permissions", permissionService.selectAll());
        return "admin/role/edit";
    }

    @RequiresPermissions("role:edit")
    @PostMapping("/edit")
    public String update(Integer id, String name, Integer[] permissionIds) {
        roleService.update(id, name, permissionIds);
        return redirect("/admin/role/list");
    }

    @RequiresPermissions("role:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        // Demande d'abord s'il y a un utilisateur associé à ce rôle. En fait, ce rôle a déjà été associé dans la base de données. Si un utilisateur est associé à ce rôle, il ne peut pas être supprimé et la base de données signalera une erreur
        // Mais ici pour l'expérience utilisateur, ajoutez une vérification
        List<AdminUser> adminUsers = adminUserService.selectByRoleId(id);
        if (adminUsers.size() > 0) {
            List<String> usernames = adminUsers.stream().map(AdminUser::getUsername).collect(Collectors.toList());
            String s = StringUtils.collectionToCommaDelimitedString(usernames);
            return error("Plusieurs clients communiquer avec lui：" + s + " Vous ne pouvez pas lui supprimer？");
        } else {
            roleService.delete(id);
            return success();
        }
    }
}
