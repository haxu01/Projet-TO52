<#include "../layout/layout.ftl">
<@html page_title="Ajouter" page_tab="auth_role">
    <section class="content-header">
        <h1>
            Role
            <small>Ajouter</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/role/list">Role</a></li>
            <li class="active">Ajouter</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Ajouter une role</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form id="form" action="/admin/role/add" method="post">
                    <div class="form-group">
                        <label>role name</label>
                        <input type="text" name="name" class="form-control">
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
                    <button type="submit" class="btn btn-xs btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </section>
</@html>
