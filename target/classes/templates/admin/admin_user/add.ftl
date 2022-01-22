<#include "../layout/layout.ftl">
<@html page_title="Ajouter" page_tab="auth_admin_user">
    <section class="content-header">
        <h1>
            User
            <small>Ajouter</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="/admin/index"><i class="fa fa-dashboard"></i> Accueil </a></li>
            <li><a href="/admin/user/list">User</a></li>
            <li class="active">Ajouter</li>
        </ol>
    </section>
    <section class="content">
        <div class="box box-info">
            <div class="box-header with-border">
                <h3 class="box-title">Ajouter un utilisateur</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
                <div class="row">
                    <div class="col-sm-6">
                        <form id="form" action="/admin/admin_user/add" method="post">
                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" id="username" name="username" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" id="password" name="password" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>Role</label>
                                <p>
                                    <#list roles as role>
                                        <input type="radio" name="roleId" value="${role.id}" id="role_${role.id}">&nbsp;
                                        <label for="role_${role.id}">${role.name!}</label>
                                    </#list>
                                </p>
                            </div>
                            <button type="submit" class="btn btn-xs btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <script>
        $(function () {
            $("#form").submit(function () {
                var username = $("#username").val();
                var password = $("#password").val();
                var roleId = $("input[name='roleId']:checked").val();
                if (!username) {
                    toast('Username ne peut pas être null');
                    return false;
                }
                if (!password) {
                    toast('Password ne peut pas être null');
                    return false;
                }
                $.ajax({
                    url: '/admin/admin_user/add',
                    async: true,
                    cache: false,
                    type: 'post',
                    dataType: 'json',
                    data: {
                        username: username,
                        password: password,
                        roleId: roleId
                    },
                    success: function (data) {
                        if (data.code === 200) {
                            toast('Success');
                            setTimeout(function () {
                                window.location.href = '/admin/admin_user/list';
                            }, 1000);
                        } else {
                            toast(data.description);
                        }
                    }
                })
                return false;
            })
        })
    </script>
</@html>
