<style> html{display : none ; } </style>
<script>
   if( self == top ) {
       document.documentElement.style.display = 'block' ; 
   } else {
       top.location = self.location ; 
   }
</script>
<?php

require_once('functions.php');
redirectTohttps();

$retVal='';

if($_SERVER['REQUEST_METHOD']==='POST') {

    if(!isset($_POST["recaptcha_response_field"])) {
        $retVal("Don't try to hack my system :(");
    }

    require_once('recaptchalib.php');
    $privatekey = getenv("CAPTCHA_PRIVATE_KEY");

    $resp = recaptcha_check_answer ($privatekey,
          $_SERVER["REMOTE_ADDR"],
          $_POST["recaptcha_challenge_field"],
          $_POST["recaptcha_response_field"]);

    if (!$resp->is_valid) {
       $retVal = "Invalid CAPTCHA";
    } else {

        $username='';
        $password='';
        $operation='';
        $accounttype='';
        $captchaid='';

        if(isset($_POST['Username'])) $username = $_POST['Username'];
        if(isset($_POST['Password'])) $password = $_POST['Password'];
        if(isset($_POST['AccountType'])) $accounttype = $_POST['AccountType'];
        if(isset($_POST['Password'])) $operation = $_POST['Operation'];
        $captchaid=$privatekey;

        $POSTURL = 'Username=' . $username . '&Password=' . $password . '&Operation=' . $operation . '&AccountType=' . $accounttype . '&CaptchaId=' . $captchaid;
        $result = doProxy($POSTURL);

        if(preg_match('/Success/i', $result)) {
            header("Location: index.php");
        }
    }  
}
?>
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
        <div class="formbox" id="registerdiv">
          <form method="POST" action="register.php" 
                name="registerform" id="registerform">
            <label for="Username">Username: </label>
            <input type="text" name="Username" id="User" /><br/>
            <label for="Password">Password: </label>
            <input type="password" name="Password" id="Password" /><br/>
            <?php
                require_once("recaptchalib.php");
                $publickey = "6LcksdgSAAAAAMw0IiovyZMDjGhEen6ZSFNZeAqw";
                echo recaptcha_get_html($publickey, null, true);
            ?>
            <input type="hidden" name="Operation" value="REGISTER"/>
            <input type="hidden" name="AccountType" value="UCHAT"/>
            <input type="submit" value="Register" />
          </form>
          <label id="Warn" class="warn" name="Warn"><?php if(isset($retVal)) {echo $retVal;} ?></label>
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
