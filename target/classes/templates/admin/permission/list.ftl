<#include "../layout/layout.ftl">
<@html page_title="Permission" page_tab="auth_permission">
    <section class="content-header">
        <h1>
            Permission
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/permission/list">Permisssion</a></li>
            <li class="active">List</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">List de permission</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <div class="row">
                    <div class="col-md-2">
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                <#if sec.hasPermission("permission:add")>
                                    <a class="pull-right" onclick="addPermission(0)" href="javascript:;"> Ajouter le node</a>
                                </#if>
                            </div>
                            <div class="list-group">
                                <#list permissions as permission>
                                    <li class="list-group-item permission-item <#if pid?? && pid == permission.id>active</#if>">
                                        <#if sec.hasPermission("permission:delete")>
                                            <a href="javascript:;" class="text-danger"
                                               onclick="deletePermission(${permission.id})">Supprimer</a>
                                        </#if>
                                        <a href="/admin/permission/list?pid=${permission.id!}">
                                            ${permission.name!}
                                        </a>
                                    </li>
                                </#list>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-10">
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                Management de permission
                                <#if sec.hasPermission("permission:add")>
                                    <a class="pull-right" onclick="addPermission(${pid!0})" href="javascript:;">Ajouter permission</a>
                                </#if>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-striped table-responsive">
                                    <thead>
                                    <tr>
                                        <th>name</th>
                                        <th>value</th>
                                        <th>Operation</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list childPermissions as permission>
                                        <tr>
                                            <td>${permission.name!}</td>
                                            <td>${permission.value!}</td>
                                            <td>
                                                <#if sec.hasPermission("permission:edit")>
                                                    <button onclick="editPermission(${permission.id}, '${permission.name}', '${permission.value}', ${permission.pid})"
                                                            class="btn btn-xs btn-warning">Editer
                                                    </button>
                                                </#if>
                                                <#if sec.hasPermission("permission:delete")>
                                                    <button onclick="deletePermission(${permission.id})"
                                                            class="btn btn-xs btn-danger">Supprimer
                                                    </button>
                                                </#if>
                                            </td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <#if sec.hasPermissionOr("permission:add", "permission:edit")>
            <button class="hidden" id="openModal" data-toggle="modal" data-target="#myModal"></button>
            <!-- Modal -->
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog modal-md" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                        aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Editer la permission</h4>
                        </div>
                        <div class="modal-body">
                            <form onsubmit="return;" id="form">
                                <input type="hidden" name="id" id="id" value=""/>
                                <input type="hidden" name="pid" id="pid" value=""/>
                                <div class="from-group">
                                    <label for="name">Name</label>
                                    <input type="text" name="name" id="name" value="" class="form-control"/>
                                </div>
                                <div class="from-group">
                                    <label for="name">Value</label>
                                    <input type="text" name="value" id="value" value="" class="form-control"/>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-info" data-dismiss="modal">Close</button>
                            <button type="button" onclick="savePermission()" class="btn btn-primary">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        </#if>
    </section>
    <script>
        <#if sec.hasPermission("permission:add")>

        function addPermission(pid) {
            $("#pid").val(pid);
            $("#openModal").click();
        }

        </#if>
        <#if sec.hasPermission("permission:edit")>

        function editPermission(id, name, value, pid) {
            $("#id").val(id);
            $("#name").val(name);
            $("#value").val(value);
            $("#pid").val(pid);
            $("#openModal").click();
        }

        </#if>
        <#if sec.hasPermission("permission:delete")>

        function deletePermission(id) {
            if (confirm('Sûr?')) {
                $.get("/admin/permission/delete?id=" + id, function (data) {
                    if (data.code === 200) {
                        toast("success");
                        setTimeout(function () {
                            window.location.href = "/admin/permission/list";
                        }, 700);
                    } else {
                        toast(data.description);
                    }
                })
            }
        }

        </#if>
        <#if sec.hasPermissionOr("permission:add", "permission:edit")>

        function savePermission() {
            var name = $("#name").val();
            var value = $("#value").val();
            if (!name) {
                toast("error");
                return;
            }
            if (!value) {
                toast("error");
                return;
            }
            var id = $("#id").val();
            var url = "/admin/permission/add";
            if (id) url = "/admin/permission/edit";
            $.post(url, $("#form").serialize(), function (data) {
                if (data.code === 200) {
                    toast("success");
                    setTimeout(function () {
                        window.location.href = "/admin/permission/list?pid=" + data.detail;
                    }, 700);
                } else {
                    toast(data.description);
                }
            })
        }

        </#if>
    </script>
</@html>
