<#include "../layout/layout.ftl">
<@html page_title="Editer" page_tab="user">
    <section class="content-header">
        <h1>
            User
            <small>Editer</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/user/list">User</a></li>
            <li class="active">Editer</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Editer un client</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <form id="form" onsubmit="return;">
                    <input type="hidden" name="id" value="${user.id}">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" id="username" name="username" value="${user.username!}"
                               class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" id="password" name="password" value="" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label>Points</label>
                        <input type="number" pattern="\d" id="score" name="score" value="${user.score!0}"
                               class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="email" name="email" value="${user.email!}" class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label>telegramName</label>
                        <input type="text" id="telegramName" name="telegramName" value="${user.telegramName!}"
                               class="form-control"/>
                    </div>
                    <div class="form-group">
                        <label>Website</label>
                        <input type="text" id="website" name="website" value="${user.website!}" class="form-control"/>
                    </div>
                    <#if sec.hasPermission("user:refresh_token")>
                        <div class="form-group">
                            <label>Token</label>
                            <div class="input-group">
                                <input type="text" id="token" name="token" value="${user.token!}" class="form-control"
                                       placeholder="Token"/>
                                <span class="input-group-btn">
                  <button type="button" onclick="refreshToken(this)" class="btn btn-info" autocomplete="off"
                          data-loading-text="Refresh">Refresh Token</button>
                  <script>
                    function refreshToken(self) {
                        $(self).button("loading");
                        $.get("/admin/user/refreshToken?id=${user.id}", function (data) {
                            console.log(data)
                            if (data.code === 200) {
                                toast("Success", "success");
                                $("#token").val(data.detail);
                            } else {
                                toast(data.description);
                            }
                            $(self).button("reset");
                        });
                    }
                  </script>
                </span>
                            </div>
                        </div>
                    </#if>
                    <div class="form-group">
                        <label>Personal Description</label>
                        <textarea name="bio" id="bio" rows="3" class="form-control">${user.bio!?html}</textarea>
                    </div>
                    <button type="button" id="btn" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    </section>
    <script>
        $(function () {
            $("#btn").click(function () {
                $.post("/admin/user/edit", $("#form").serialize(), function (data) {
                    if (data.code === 200) {
                        toast("Success", "success");
                        setTimeout(function () {
                            window.location.href = "/admin/user/list";
                        }, 700);
                    } else {
                        toast(data.description);
                    }
                })
            });
        })
    </script>
</@html>
