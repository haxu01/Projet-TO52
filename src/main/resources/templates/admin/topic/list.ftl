<#include "../layout/layout.ftl">
<@html page_title="List" page_tab="topic">
    <section class="content-header">
        <h1>
            Topic
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/topic/list">Topic</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">List</h3>
                <span class="pull-right">
          <#if sec.hasPermission("topic:index_all")>
              <button onclick="index_all_topic()" class="btn btn-primary btn-xs">Index all</button>&nbsp;
          </#if>
                    <#if sec.hasPermission("topic:delete_all_index")>
                        <button onclick="delete_all_index()" class="btn btn-danger btn-xs">Delete all index</button>
                    </#if>
        </span>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form action="/admin/topic/list" class="form-inline">
                    <div class="form-group" style="margin-bottom: 10px;">
                        <input type="text" readonly id="startDate" name="startDate" value="${startDate!}"
                               class="form-control">
                        <input type="text" readonly id="endDate" name="endDate" value="${endDate!}"
                               class="form-control">
                        <input type="text" name="username" value="${username!}" class="form-control">
                        <button type="submit" class="btn btn-primary btn-sm">Rechercher</button>
                    </div>
                </form>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Topic</th>
                        <th>User</th>
                        <th>Number of comments</th>
                        <th>Status</th>
                        <th>Time</th>
                        <th>Opération</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list page.records as topic>
                        <tr>
                            <td>${topic.id}</td>
                            <td><a href="/topic/${topic.id}" target="_blank">${topic.title}</a></td>
                            <td><a href="/user/${topic.username}" target="_blank">${topic.username}</a></td>
                            <td>${topic.commentCount}</td>
                            <td>
                                <#if topic.top>
                                    Top
                                <#elseif topic.good>
                                    GOOD
                                <#else>
                                    &nbsp;
                                </#if>
                            </td>
                            <td>${topic.inTime!}</td>
                            <td>
                                <#if sec.hasPermission("topic:index")>
                                    <button onclick="index_topic('${topic.id}')" class="btn btn-xs btn-primary">Index
                                    </button>
                                </#if>
                                <#if sec.hasPermission("topic:delete_index")>
                                    <button onclick="delete_index('${topic.id}')" class="btn btn-xs btn-danger">Delete Index
                                    </button>
                                </#if>
                                <#if sec.hasPermission("topic:top")>
                                    <button onclick="actionBtn('${topic.id}', 'top', this)"
                                            class="btn btn-xs btn-warning">
                                        <#if topic.top>
                                            Cancel Top
                                        <#else>
                                            Top
                                        </#if>
                                    </button>
                                </#if>
                                <#if sec.hasPermission("topic:good")>
                                    <button onclick="actionBtn('${topic.id}', 'good', this)"
                                            class="btn btn-xs btn-warning">
                                        <#if topic.good>
                                            Cancel Good
                                        <#else>
                                            Good
                                        </#if>
                                    </button>
                                </#if>
                                <#if sec.hasPermission("topic:edit")>
                                    <a href="/admin/topic/edit?id=${topic.id}" class="btn btn-xs btn-warning">Editer</a>
                                </#if>
                                <#if sec.hasPermission("topic:delete")>
                                    <button onclick="actionBtn('${topic.id}', 'delete', this)"
                                            class="btn btn-xs btn-danger">Delete
                                    </button>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <#include "../layout/paginate.ftl">
        <@paginate currentPage=page.current totalPage=page.pages actionUrl="/admin/topic/list"
        urlParas="&startDate=${startDate!}&endDate=${endDate!}&username=${username!}"/>
    </section>
    <script>
        $(function () {
            $("#startDate").datepicker({
                language: 'zh-CN',
                autoclose: true,
                format: 'yyyy-mm-dd',
                todayBtn: true,
                todayHighlight: true,
            });
            $("#endDate").datepicker({
                language: 'zh-CN',
                autoclose: true,
                format: 'yyyy-mm-dd',
                todayBtn: true,
                todayHighlight: true,
            });
        });
        <#if sec.hasPermissionOr("topic:top", "topic:good", "topic:delete")>

        function actionBtn(id, action, self) {
            var msg, url;
            var tip = $(self).text().replace(/[\r\n]/g, '').trim();
            if (action === 'top') {
                url = '/admin/topic/top?id=' + id;
                msg = 'Are u sure' + tip + 'this comment';
            } else if (action === 'good') {
                url = '/admin/topic/good?id=' + id;
                msg = 'Are u sure' + tip + 'this comment';
            } else if (action === 'delete') {
                url = '/admin/topic/delete?id=' + id;
                msg = 'Are u sure delete this comment';z
            }
            if (confirm(msg)) {
                $.get(url, function (data) {
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
        <#if sec.hasPermission("topic:index")>

        function index_topic(id) {
            if (confirm("S")) {
                $.get("/admin/topic/index?id=" + id, function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                    } else {
                        toast(data.description);
                    }
                });
            }
        }

        </#if>
        <#if sec.hasPermission("topic:index_all")>

        function index_all_topic() {
            if (confirm("Sûr?")) {
                $.get("/admin/topic/index_all", function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                    } else {
                        toast(data.description);
                    }
                })
            }
        }

        </#if>
        <#if sec.hasPermission("topic:delete_all_index")>

        function delete_all_index() {
            if (confirm("Sûr?")) {
                $.get("/admin/topic/delete_all_index", function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                    } else {
                        toast(data.description);
                    }
                })
            }
        }

        </#if>
        <#if sec.hasPermission("topic:delete_index")>

        function delete_index(id) {
            if (confirm("Sûr")) {
                $.get("/admin/topic/delete_index?id=" + id, function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                    } else {
                        toast(data.description);
                    }
                })
            }
        }

        </#if>
    </script>
</@html>
