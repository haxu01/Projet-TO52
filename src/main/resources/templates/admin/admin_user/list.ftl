<#include "../layout/layout.ftl">
<@html page_title="List" page_tab="auth_admin_user">
    <section class="content-header">
        <h1>
            Username
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/user/list">Username</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">List des utilisateurs</h3>
                <#if sec.hasPermission('admin_user:add')>
                    <a href="/admin/admin_user/add" class="btn btn-xs btn-primary pull-right">Ajouter</a>
                </#if>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Date d'inscription</th>
                        <th>Opération</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list adminUsers as adminUser>
                        <tr>
                            <td>${adminUser.id}</td>
                            <td>${adminUser.username!}</td>
                            <td>${adminUser.roleName!}</td>
                            <td>${adminUser.inTime?datetime}</td>
                            <td>
                                <#if sec.hasPermission('admin_user:edit')>
                                    <a href="/admin/admin_user/edit?id=${adminUser.id}" class="btn btn-xs btn-warning">Editer</a>
                                </#if>
                                <#if sec.hasPermission('admin_user:delete')>
                                    <a href="javascript:if(confirm('Vous êtes sûr?')) location.href='/admin/admin_user/delete?id=${adminUser.id}'"
                                       class="btn btn-xs btn-danger">Supprimer</a>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</@html>
