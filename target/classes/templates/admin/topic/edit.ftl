<#include "../layout/layout.ftl">
<@html page_title="Editer" page_tab="topic">
    <section class="content-header">
        <h1>
            Topic
            <small>Editer</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/topic/list">Topic</a></li>
            <li class="active">Editer</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Editer un topic</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form action="" onsubmit="return;" id="form">
                    <div class="form-group">
                        <label for="title">Topic</label>
                        <input type="text" name="title" id="title" value="${topic.title}" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label for="content">Content</label>
                        <#if topic.style == "MD">
                            <link href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/codemirror.min.css" rel="stylesheet">
                            <style>
                                .CodeMirror {
                                    border: 1px solid #ddd;
                                }
                            </style>
                            <script src="/static/theme/default/js/codemirror.js"></script>
                            <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/mode/markdown/markdown.min.js"></script>
                            <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/addon/display/placeholder.min.js"></script>
                            <textarea name="content" id="content" class="form-control">${topic.content!?html}</textarea>
                            <script type="text/javascript">
                                CodeMirror.keyMap.default["Shift-Tab"] = "indentLess";
                                CodeMirror.keyMap.default["Tab"] = "indentMore";
                                var editor = CodeMirror.fromTextArea(document.getElementById("content"), {
                                    lineNumbers: true,
                                    indentUnit: 4,
                                    tabSize: 4,
                                    matchBrackets: true,
                                    mode: 'markdown',
                                    lineWrapping: true,
                                });
                            </script>
                        <#elseif topic.style == "RICH">
                            <script src="https://cdnjs.cloudflare.com/ajax/libs/wangEditor/3.1.1/wangEditor.min.js"></script>
                            <div id="content">${topic.content!}</div>
                            <script type="text/javascript">
                                const E = window.wangEditor;
                                window._E = new E(document.getElementById("content"));
                                window._E.create();
                                window._E.config.height = 500;
                            </script>
                        </#if>
                    </div>
                    <div class="form-group">
                        <label for="tags">Tag</label>
                        <input type="text" name="tags" id="tags" value="${tags}" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <button type="button" id="btn" class="btn btn-primary">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </section>
    <script>
        $(function () {
            $("#btn").click(function () {
                var title = $("#title").val();
                var content = window.editor ? window.editor.getDoc().getValue() : window._E.txt.html();
                var tags = $("#tags").val();
                if (!title || title.length > 120) {
                    toast("moin 120 mots");
                    return;
                }
                if (!tags || tags.split(",").length > 5) {
                    toast("5 au maximum");
                    return;
                }
                $.post("/admin/topic/edit", {
                    id: ${topic.id},
                    title: title,
                    content: content,
                    tags: tags,
                }, function (data) {
                    if (data.code === 200) {
                        toast("success", "success");
                        setTimeout(function () {
                            window.location.href = "/admin/topic/list";
                        }, 700);
                    } else {
                        toast(data.description);
                    }
                })
            })
        })
    </script>
</@html>
