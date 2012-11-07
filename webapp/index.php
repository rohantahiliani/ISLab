<?php
   require_once('functions.php');
   redirectTohttps();

   if(isset($_COOKIE['FailedRequests']) && intval($_COOKIE['FailedRequests']) > 2) {
        header('Location: solvecaptcha.php');
   }
?>
<style> html{display : none ; } </style>
<script>
   if( self == top ) {
       document.documentElement.style.display = 'block' ; 
   } else {
       top.location = self.location ; 
   }
</script>

<html>

  <head>
    <title>UXChat</title>
    <link rel="Stylesheet" href="stylesheets/elements.css" type="text/css" />
    <link rel="Stylesheet" href="stylesheets/classes.css" type="text/css" />
    <link rel="Stylesheet"  href="stylesheets/jquery-ui.css" type="text/css" />
  </head>

  <body>

    <div class="main">
      <div class="header">
        <h1>UXChat</h1>
      </div>

      <div class="content">
        <div class="formbox" id="logindiv">
          <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
                name="loginform" id="loginform" autocomplete="off">
            <label for="Username">Username:<img src="" id="UsernameStatus" /></label>
            <input type="text" name="Username" id="Username" /><br/>
            
            <label for="Password">Password:<img src="" id="PasswordStatus" /></label>
            <input type="password" name="Password" id="Password" /><br/>
            <input type="hidden" name="Operation" value="LOGIN"/>
            <input type="hidden" name="AccountType" value="UCHAT"/>
            <input type="submit" value="Chat!" />
          </form>
          <br/><center>OR</center>
          <form method="GET" action="register.php">
            <input type="submit" value="Register" />
          </form>
        </div>
      </div>

      <div class="footer">
        <img src="images/gtalk.png" alt="Gtalk" class="logo" />
        <img src="images/jabber.png" alt="Jabber" class="logo" /> 
      </div>
    </div>

    <script src="js/jquery/jquery.js"></script>
    <script src="js/jquery/jquery.cookie.js"></script>
    <script src="js/jquery/jquery.form.js"></script>
    <script src="js/jquery/jquery-ui.js"></script>
    <script src="js/functions.js"></script>
  </body>

</html>
