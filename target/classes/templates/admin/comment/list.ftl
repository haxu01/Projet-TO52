<#include "../layout/layout.ftl">
<@html page_title="List" page_tab="comment">
    <section class="content-header">
        <h1>
            Comment
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/comment/list">Comment</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">List des commentaires</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form action="/admin/comment/list" class="form-inline">
                    <div class="form-group" style="margin-bottom: 10px;">
                        <input type="text" readonly id="startDate" name="startDate" value="${startDate!}"
                               class="form-control">
                        <input type="text" readonly id="endDate" name="endDate" value="${endDate!}"
                               class="form-control">
                        <input type="text" name="username" value="${username!}" class="form-control" >
                        <button type="submit" class="btn btn-primary btn-sm">Rechercher</button>
                    </div>
                </form>
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Topic</th>
                        <th>User</th>
                        <th>Time</th>
                        <th>Opération</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.records as comment>
                        <tr>
                            <td>${comment.id}</td>
                            <td><a href="/topic/${comment.topicId}" target="_blank">${comment.title!}</a></td>
                            <td>${comment.username!}</td>
                            <td>${comment.inTime!}</td>
                            <td>
                                <#if sec.hasPermission("comment:edit")>
                                    <a href="/admin/comment/edit?id=${comment.id}" class="btn btn-xs btn-warning">Editer</a>
                                </#if>
                                <#if sec.hasPermission("comment:delete")>
                                    <button onclick="deleteBtn('${comment.id}')" class="btn btn-xs btn-danger">Supprimer
                                    </button>
                                </#if>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="5">${comment.content}</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <#include "../layout/paginate.ftl">
        <@paginate currentPage=page.current totalPage=page.pages actionUrl="/admin/comment/list"
        urlParas="&startDate=${startDate!}&endDate=${endDate!}&username=${username!}"/>
    </section>
    <script>
        $(function () {
            $("#startDate").datepicker({
                language: 'en-US',
                autoclose: true,
                format: 'yyyy-mm-dd',
                todayBtn: true,
                todayHighlight: true,
            });
            $("#endDate").datepicker({
                language: 'en-US',
                autoclose: true,
                format: 'yyyy-mm-dd',
                todayBtn: true,
                todayHighlight: true,
            });
        });
        <#if sec.hasPermission("comment:delete")>

        function deleteBtn(id) {
            if (confirm('Vous êtes sûr?')) {
                $.get("/admin/comment/delete?id=" + id, function (data) {
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
