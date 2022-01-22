<#include "../layout/layout.ftl">
<@html page_title="Editer" page_tab="auth_role">
    <section class="content-header">
        <h1>
            Role
            <small>Editer</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/role/list">Role</a></li>
            <li class="active">Editer</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Editer un role</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form id="form" action="/admin/role/edit" method="post">
                    <input type="hidden" name="id" value="${role.id}">
                    <div class="form-group">
                        <label>Role name</label>
                        <input type="text" name="name" value="${role.name}" class="form-control">
                    </div>
                    <div class="form-group">
                        <#list permissions?keys as key>
                            <label for="">${key}</label>
                            <p>
                                <#list permissions[key] as permission>
                                    <input type="checkbox" name="permissionIds" id="permission_${permission.id}"
                                           value="${permission.id}"/>
                                    <label for="permission_${permission.id}">${permission.name}</label>
                                </#list>
                            </p>
                        </#list>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </section>
    <script>
        $(function () {
            <#list currentPermissions as permission>
            $("#permission_${permission.id}").attr("checked", true);
            </#list>
        })
    </script>
</@html>
