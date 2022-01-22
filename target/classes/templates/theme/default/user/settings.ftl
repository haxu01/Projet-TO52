<#include "../layout/layout.ftl"/>
<@html page_title="Settings" page_tab="settings">
    <div class="row">
        <div class="col-md-9">
            <#if !user.active>
                <div class="alert alert-danger">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                </div>
            </#if>
            <div class="card">
                <div class="card-header">Setting</div>
                <div class="card-body">
                    <br>
                    <form class="form-horizontal" onsubmit="return;">
                        <div class="form-group row">
                            <label for="username" class="col-sm-2 control-label">Username</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="username" name="username" disabled
                                       value="${user.username}">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="telegramName" class="col-sm-2 control-label">Telegram</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="telegramName" name="telegramName"
                                       value="${user.telegramName!}">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="website" class="col-sm-2 control-label">Website</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="website" name="website"
                                       value="${user.website!}">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="bio" class="col-sm-2 control-label">Introduction personnelle</label>
                            <div class="col-sm-10">
                                <textarea name="bio" id="bio" rows="3" class="form-control">${user.bio!?html}</textarea>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" id="emailNotification"
                                               <#if user.emailNotification>checked</#if>>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-10">
                                <button type="button" id="settings_btn" class="btn btn-info">Submit</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="card">
                <div class="card-header">Changer le mail</div>
                <div class="card-body">
                    <form onsubmit="return;" class="form-horizontal">
                        <div class="form-group row">
                            <label for="email" class="col-sm-2 control-label">Email</label>
                            <div class="col-sm-10">
                                <div class="input-group">
                                    <input type="email" name="email" id="email" class="form-control" value="${user.email!}"/>
                                    <span class="input-group-append">
                                        <button type="button" id="sendEmailCode" class="btn btn-info" autocomplete="off">Captcha</button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="code" class="col-sm-2 control-label">Captcha</label>
                            <div class="col-sm-10">
                                <input type="text" name="code" id="code" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-10">
                                <button type="button" id="settings_email_btn" class="btn btn-info">Changer le mail</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="card">
                <div class="card-header">Changer mon avatar</div>
                <div class="card-body">
                    <form onsubmit="return;" class="form-horizontal">
                        <div class="form-group row">
                            <label for="" class="col-sm-2 control-label" style="vertical-align: middle">L’avatar actuel</label>
                            <div class="col-sm-10">
                                <img src="${user.avatar!}" class="avatar avatar-lg" alt="avatar"/>&nbsp;&nbsp;
                                <img src="${user.avatar!}" class="avatar" style="vertical-align: bottom" alt="avatar"/>&nbsp;&nbsp;
                                <img src="${user.avatar!}" class="avatar avatar-sm" style="vertical-align: bottom" alt="avatar"/>
                            </div>
                            <div class="offset-sm-2 col-sm-10" style="margin-top: 10px;">
                                <a href="javascript:;" id="selectAvatar">Télécharger le nouvel avatar</a>
                                <input type="file" class="d-none" name="file" id="file"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="card">
                <div class="card-header">Changer le mot de passe</div>
                <div class="card-body">
                    <form onsubmit="return;" class="form-horizontal">
                        <div class="form-group row">
                            <label for="oldPassword" class="col-sm-2 control-label">Ancien mot de passe</label>
                            <div class="col-sm-10">
                                <input type="password" name="oldPassword" id="oldPassword" class="form-control" />
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="newPassword" class="col-sm-2 control-label">Nouvel mot de passe</label>
                            <div class="col-sm-10">
                                <input type="password" name="newPassword" id="newPassword" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="offset-sm-2 col-sm-10">
                                <button type="button" id="settings_pwd_btn" class="btn btn-info">Changer</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-3 hidden-xs">
            <#include "../components/user_info.ftl"/>
            <#include "../components/token.ftl"/>
        </div>
    </div>
    <script>
        $(function () {
            $("#settings_btn").click(function () {
                var telegramName = $("#telegramName").val();
                var website = $("#website").val();
                var bio = $("#bio").val();
                var emailNotification = $("#emailNotification").is(":checked");
                req("put", "/api/settings", {
                    telegramName: telegramName,
                    website: website,
                    bio: bio,
                    emailNotification: emailNotification,
                }, "${_user.token!}", function (data) {
                    if (data.code === 200) {
                        suc("Success");
                        setTimeout(function () {
                            window.location.reload();
                        }, 700);
                    } else {
                        err(data.description);
                    }
                });
            });


            $("#selectAvatar").click(function () {
                $("#file").click();
            });
            $("#file").change(function () {
                var fd = new FormData();
                fd.append("file", document.getElementById("file").files[0]);
                fd.append("type", "avatar");
                fd.append("token", "${_user.token!}");
                $.post({
                    url: "/api/upload",
                    data: fd,
                    dataType: 'json',
                    headers: {
                        'token': '${_user.token!}'
                    },
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        if (data.code === 200) {
                            if (data.detail.errors.length === 0) {
                                suc("success");
                                $.each($(".avatar "), function (i, v) {
                                    $(v).attr("src", data.detail.urls[0]);
                                })
                            } else {
                                err(data.detail.errors[0]);
                            }
                        } else {
                            err(data.description);
                        }
                    }
                })
            });

            $("#sendActiveEmail").on("click", function () {
                req("get", "/api/settings/sendActiveEmail", "${_user.token!}", function (data) {
                    if (data.code === 200) {
                    } else {
                        err(data.description);
                    }
                });
            })

            $("#sendEmailCode").on("click", function () {
                var loadingBtn = $(this).button("loading");
                var email = $("#email").val();
                req("get", "/api/settings/sendEmailCode", {email}, "${_user.token!}", function (data) {
                    if (data.code === 200) {
                    } else {
                        err(data.description);
                    }
                    loadingBtn.button("reset");
                });
            })
            $("#settings_email_btn").click(function () {
                var email = $("#email").val();
                var code = $("#code").val();
                req("put", "/api/settings/updateEmail", {email, code}, "${_user.token!}", function (data) {
                    if (data.code === 200) {
                        suc("success");
                        setTimeout(function () {
                            window.location.reload();
                        }, 700);
                    } else {
                        err(data.description);
                    }
                });
            })

            $("#settings_pwd_btn").click(function () {
                var oldPassword = $("#oldPassword").val();
                var newPassword = $("#newPassword").val();
                if (!oldPassword) {
                    err("error");
                    return;
                }
                if (!newPassword) {
                    err("error");
                    return;
                }
                req("put", "/api/settings/updatePassword", {oldPassword, newPassword}, "${_user.token!}", function (data) {
                    if (data.code === 200) {
                        suc("success");
                    } else {
                        err(data.description);
                    }
                });
            });
        })
    </script>
</@html>
