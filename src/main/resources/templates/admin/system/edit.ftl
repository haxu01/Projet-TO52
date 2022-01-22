<#include "../layout/layout.ftl">
<style>
    .tp-target:target {
        position: relative;
        top: -50px;
        display: block;
        height: 0;
        overflow: hidden;
    }
</style>
<@html page_title="Settings" page_tab="system">
    <section class="content-header">
        <h1>
            System
            <small>Setting</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/system/edit">System</a></li>
            <li class="active">Setting</li>
        </ol>
    </section>
    <section class="content" style="margin-bottom: 40px;">
        <form id="form" onsubmit="return;">
            <#list systems?keys as key>
                <div class="tp-target" id="${key}"></div>
                <div class="form-group">
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">${key}</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <#list systems[key] as system>
                                <div>
                                    <h5>${system.description!}</h5>
                                    <#--<input type="hidden" name="key" value="${system.key}" class="form-control"/>-->
                                    <#if system.type == "email">
                                        <input type="email" id="${system.key!}" name="${system.key!}"
                                               value="${system.value!}"
                                               class="form-control"/>
                                    <#elseif system.type == "text">
                                        <input type="text" id="${system.key!}" name="${system.key!}"
                                               value="${system.value!}"
                                               class="form-control"/>
                                    <#elseif system.type == "password">
                                        <input type="password" id="${system.key!}" name="${system.key!}"
                                               value="<#if system.value?? && system.value != ''>*******</#if>"
                                               class="form-control"/>
                                    <#elseif system.type == "url">
                                        <input type="url" id="${system.key!}" name="${system.key!}"
                                               value="${system.value!}"
                                               class="form-control"/>
                                    <#elseif system.type == "number">
                                        <input type="number" id="${system.key!}" name="${system.key!}"
                                               value="${system.value!}"
                                               class="form-control"/>
                                    <#elseif system.type == "radio">
                                        <input type="radio" id="${system.key!}1" name="${system.key!}"
                                               <#if system.value == "1">checked</#if> value="1"/>
                                        <label for="${system.key!}1">是</label>&nbsp;&nbsp;
                                        <input type="radio" id="${system.key!}0" name="${system.key!}"
                                               <#if system.value == "0">checked</#if> value="0"/>
                                        <label for="${system.key!}0">否</label>
                                    <#elseif system.type == "select">
                                        <select id="${system.key!}" name="${system.key!}" class="form-control">
                                            <#list system.option?split(',') as opt>
                                                <option <#if system.value == opt>selected</#if>
                                                        value="${opt}">${opt}</option>
                                            </#list>
                                        </select>
                                    </#if>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </#list>
        </form>
        <div class="form-group" style="position: fixed; bottom: 50px;">
            <button type="button" onclick="save()" class="btn btn-primary">Submit</button>&nbsp;
            <div class="btn-group dropup">
                <ul class="dropdown-menu">
                    <#list systems?keys as key>
                        <li><a href="#${key}">${key}</a></li>
                    </#list>
                </ul>
            </div>
        </div>
    </section>
    <script>
        function save() {
            var search = $("#search").val();
            var es_host = $("#elasticsearch_host").val();
            var es_port = $("#elasticsearch_port").val();
            var es_index = $("#elasticsearch_index").val();
            if (search === "1" && (es_host.length === 0 || es_port.length === 0 || es_index.length === 0)) {
                toast("error");
            } else {
                $.ajax({
                    url: "/admin/system/edit",
                    contentType: "application/json; charset=utf-8",
                    type: 'post',
                    dataType: 'json',
                    data: JSON.stringify($("#form").serializeArray()),
                    success: function (data) {
                        if (data.code === 200) {
                            toast("success", "success");
                            setTimeout(function () {
                                window.location.reload();
                            }, 700);
                        } else {
                            toast(data.description);
                        }
                    }
                })
            }
        }
    </script>
</@html>
