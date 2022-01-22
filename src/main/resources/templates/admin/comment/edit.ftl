<#include "../layout/layout.ftl">
<@html page_title="Editer" page_tab="comment">
    <section class="content-header">
        <h1>
            Comment
            <small>List</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/comment/list">Comment</a></li>
            <li class="active">Editer</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Editer le commentaire</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <div class="form-group">
                    <textarea name="content" id="content" class="form-control">${comment.content?html}</textarea>
                </div>
                <div class="form-group">
                    <button type="button" id="btn" class="btn btn-primary">
                        <span class="glyphicon glyphicon-send"></span> Update
                    </button>
                </div>
            </div>
        </div>
    </section>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/codemirror.min.css" rel="stylesheet">
    <style>
        .CodeMirror {
            border: 1px solid #ddd;
        }
    </style>
    <script src="/static/theme/default/js/codemirror.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/mode/markdown/markdown.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.47.0/addon/display/placeholder.min.js"></script>
    <script>
        $(function () {
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

            $("#btn").click(function () {
                var content = editor.getDoc().getValue();
                if (!content) {
                    toast("");
                    return;
                }
                $.post("/admin/comment/edit", {
                    id: ${comment.id},
                    content: content
                }, function (data) {
                    if (data.code === 200) {
                        toast("success");
                        setTimeout(function () {
                            window.location.href = "/admin/comment/list";
                        }, 700);
                    } else {
                        toast(data.description);
                    }
                })
            })
        });
    </script>
</@html>
