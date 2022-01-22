<div class="card">
    <div class="card-header">
        Token
        <a href="javascript:;" id="refreshToken" class="pull-right">Token</a>
    </div>
    <div class="card-body">
        <p>Token：<code id="userToken">${_user.token!}</code></p>
        <div id="qrcode" class="text-center"></div>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    $("#qrcode").qrcode({
        width: 180,
        height: 180,
        text: '${_user.token!}'
    });

    $("#refreshToken").on("click", function () {
        req("get", "/api/settings/refreshToken", $("#userToken").text(), function (data) {
            if (data.code === 200) {
                suc("success");
                window.location.reload();
            } else {
                err("error");
            }
        })
    });
</script>
