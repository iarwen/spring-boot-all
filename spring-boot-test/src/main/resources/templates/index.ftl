<!DOCTYPE html>
<html lang="zh-cn">  
<head>  
<meta charset="utf-8">  
<meta http-equiv="X-UA-Compatible" content="IE=edge">  
<meta name="viewport" content="width=device-width, initial-scale=1.0">  
<meta name="description" content="">  
<meta name="author" content="">  
  
<title>用户登录</title>  
</head>  
<!-- CSS件 -->  
<link rel="stylesheet" href="/res/bootstrap/css/bootstrap.min.css">  
<body>
	Date: ${time?date}
	<br>
	Time: ${time?time}
    <div class="container">  
        <form class="form-signin">  
            <h2 class="form-signin-heading">Please sign in</h2>  
            <input type="text" class="form-control" placeholder="Email address" required autofocus>   
            <input type="password" class="form-control" placeholder="Password" required>   
            <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>  
        </form>  
    </div>  
  
    <script src="/res/jquery/jquery-1.11.0.min.js"></script>  
    <script src="/res/bootstrap/js/bootstrap.min.js"></script>  
</body>  
</html>