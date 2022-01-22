<#include "../layout/layout.ftl"/>
<@html page_title="User" page_tab="user">
    <section class="content-header">
        <h1>
            User
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/user/list">User</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">User List</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form action="/admin/user/list" class="form-inline">
                    <div class="form-group" style="margin-bottom: 10px;">
                        <input type="text" id="username" name="username" value="${username!}" class="form-control">
                        <button type="submit" class="btn btn-primary btn-sm">Rechercheer</button>
                    </div>
                </form>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Points</th>
                        <th>Time</th>
                        <th>Operation</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.records as user>
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username!}</td>
                            <td>${user.email!}</td>
                            <td>${user.score!0}</td>
                            <td>${user.inTime?datetime}</td>
                            <td>
                                <#if sec.hasPermission("user:edit")>
                                    <a href="/admin/user/edit?id=${user.id}" class="btn btn-xs btn-warning">Editer</a>
                                </#if>
                                <#if sec.hasPermission("user:delete")>
                                    <button onclick="deleteUser('${user.id}')" class="btn btn-xs btn-danger">Supprimer</button>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <#include "../layout/paginate.ftl">
        <@paginate currentPage=page.current totalPage=page.pages actionUrl="/admin/user/list" urlParas=""/>
    </section>
    <script>
        <#if sec.hasPermission("user:delete")>

        function deleteUser(id) {
            if (confirm("Sûr?")) {
                $.get("/admin/user/delete?id=" + id, function (data) {
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
