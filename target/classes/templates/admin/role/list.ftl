<#include "../layout/layout.ftl">
<@html page_title="Role list" page_tab="auth_role">
    <section class="content-header">
        <h1>
            角色
            <small>列表</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/role/list">Role</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Role list</h3>
                <#if sec.hasPermission('role:add')>
                    <a href="/admin/role/add" class="btn btn-xs btn-primary pull-right">Ajouter</a>
                </#if>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Role Name</th>
                        <th>Opération</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list roles as role>
                        <tr>
                            <td>${role.id}</td>
                            <td>${role.name!}</td>
                            <td>
                                <#if sec.hasPermission('role:edit')>
                                    <a href="/admin/role/edit?id=${role.id}" class="btn btn-xs btn-warning">Editer</a>
                                </#if>
                                <#if sec.hasPermission('role:delete')>
                                    <button onclick="deleteRole(${role.id})" class="btn btn-xs btn-danger">Supprimer</button>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
    <script>
        <#if sec.hasPermission('role:delete')>

        function deleteRole(id) {
            if (confirm("Sûr？")) {
                $.get("/admin/role/delete?id=" + id, function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                        setTimeout(function () {
                            window.location.reload();
                        }, 700);
                    } else {
                        toast(data.description);
                    }
                })
            }
        }

        </#if>
    </script>
</@html>
