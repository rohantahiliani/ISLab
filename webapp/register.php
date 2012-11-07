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
        } else {
            echo $result;
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
                name="registerform" id="registerform" autocomplete="off">
            <label for="Username">Username:<img src="" id="UsernameStatus"/></label>
            <input type="text" id="Username" name="Username" id="User" /><br/>
            <label for="Password">Password:<img src="" id="PasswordStatus"/></label>
            <input type="password" id="Password" name="Password" id="Password" /><br/>
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
        <br/><br/>
        <div class="infobox">
          <h3>Registration Information</h3>
          <ul>Username must be at least 6 and at most 16 characters long.</ul>
          <ul>Username must:
            <ul>Start with an alphabet (a-z or A-Z)</ul>
            <ul>Contain only alphanumeric characters (a-z or A-Z or 0-9)</ul>
          </ul>
          <ul>Password must be at least 6 and at most 20 characters long.</ul>
          <ul>Password must contain at least:
            <ul>1 upper case alphabet (A-Z) </ul>
            <ul>1 lower case alphabet (a-z) </ul>
            <ul>1 number (0-9) </ul>
            <ul>1 special character (<b> . _ - $ # @ % </b>)</ul>
          </ul>
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
