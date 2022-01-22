<#include "../layout/layout.ftl">
<@html page_title="Editer" page_tab="tag">
    <section class="content-header">
        <h1>
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/tag/list">Tag</a></li>
            <li class="active">Editer</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Editer un tag</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form id="form" action="/admin/tag/edit" method="post" enctype="multipart/form-data">
                    <input type="hidden" value="${tag.id}" name="id">
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" value="${tag.name!}" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Number</label>
                        <input type="number" pattern="\d" name="topicCount" value="${tag.topicCount!}"
                               class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Icon</label>
                        <input type="file" name="file" class="form-control"><br>
                        <#if tag.icon??>
                            <img src="${tag.icon!}" width="50" alt="">
                        </#if>
                    </div>
                    <div class="form-group">
                        <label for="">Description</label>
                        <textarea name="description" rows="7" class="form-control">${tag.description!}</textarea>
                    </div>
                    <button type="submit" id="btn" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </section>
</@html>
