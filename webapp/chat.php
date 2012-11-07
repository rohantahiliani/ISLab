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

   if(isset($_COOKIE['FailedRequests']) && intval($_COOKIE['FailedRequests']) > 2) {
        header('Location: solvecaptcha.php');
   }

$retVal='';
$friendList = array();

if($_SERVER['REQUEST_METHOD']==='GET') {

    if(!isset($_COOKIE['UCHATUsername'])) die("Don't try to hack my system :(");
    if(!isset($_COOKIE['UCHATSessionId'])) die("Don't try to hack my system :(");

    doProxy("Operation=VALIDATE&AccountType=UCHAT");

    if(isset($_COOKIE['GOOGLEUsername']) && isset($_COOKIE['GOOGLESessionId'])) {
        $googleList = doProxy("Operation=GETFRIENDS&AccountType=GOOGLE");
        if(count($googleList) > 0) {
            $friendList ["GOOGLE"] =  $googleList;
        }
    }  
   if(isset($_COOKIE['JABBERUsername']) && isset($_COOKIE['JABBERSessionId'])) {
        $jabberList = doProxy("Operation=GETFRIENDS&AccountType=JABBER");
        if(count($jabberList) > 0) {
            $friendList ["JABBER"] =  $jabberList;
        }
    } 

} else {
   die("Don't try to hack my system");
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
        <div class="contactbar" name="contactbar">
          <div id="UChatUsername"></div>
          Contacts <hr/>
          <?php
             if(isset($friendList)) {
              foreach($friendList as $key => $value) {
                  echo "<small>".$key."<div id='".$key."Username'></div></small><hr/>";

                  foreach($value as $friend) {
          ?>
          <a href="#" class="contactentry" atype="<?php echo $key ?>" 
                friend="<?php echo $friend ?>" onclick="return openChat(this)">
            <?php echo $friend ?>
          </a><br/>
          <?php
                  }
               }
             }
          ?>
        </div>
        <div class="divider" style="left:18%"></div>
        <div class="chatcontainer" name="chatcontainer" id="chatcontainer">
          
        </div>
      </div>

      <div class="footer">
        <div style="valign: top">
            LOGIN
        </div>
        <span>
            <img src="images/gtalk.png" alt="Gtalk" class="logo" onclick="showPopup('GTalkDiv');" /> 
        </span>
        <span>
          <img src="images/jabber.png" alt="Jabber" class="logo" onclick="showPopup('JabberDiv');"/> 
        </span>
        <span>
          <img src="images/uchat.png" alt="UChat" class="logo" onclick="showPopup('UChatDiv');"/> 
        </span>
      </div>
    </div>

    <script src="js/jquery/jquery.js"></script>
    <script src="js/jquery/jquery.cookie.js"></script>
    <script src="js/jquery/jquery.form.js"></script>
    <script src="js/jquery/jquery-ui.js"></script>
    <script src="js/functions.js"></script>

    <?php if(!isset($_COOKIE['GOOGLEUsername']) || !isset($_COOKIE['GOOGLESessionId'])) { ?>
    <div class="formbox clientlogin popup" id="GTalkDiv" >
      <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
            name="loginform" id="loginform">
        <label for="Username">Username: </label>
        <input type="text" name="Username" id="User" /><br/>
        <label for="Password">Password: </label>
        <input type="password" name="Password" id="Password" /><br/>
        <input type="hidden" name="Operation" value="LOGIN"/>
        <input type="hidden" name="AccountType" value="GOOGLE"/>
        <input type="submit" value="Chat on Gtalk!" />
      </form>
    </div>
    <?php } else { ?>
    <div class="formbox clientlogin popup" id="GTalkDiv" >
      <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
            name="logoutform" id="logoutform">
        <input type="hidden" name="Operation" value="DISCONNECT"/>
        <input type="hidden" name="AccountType" value="GOOGLE"/>
        <input type="submit" value="Logout from GTalk" />
      </form>
    </div>
    <script>
      $("#GOOGLEUsername").text($.cookie("GOOGLEUsername"));
    </script>
    <?php } ?>

    <?php if(!isset($_COOKIE['JABBERUsername']) && !isset($_COOKIE['JABBERSessionId'])) { ?>
    <div class="formbox clientlogin popup" id="JabberDiv">
      <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
            name="loginform" id="loginform">
        <label for="Username">Username: </label>
        <input type="text" name="Username" id="User" /><br/>
        <label for="Password">Password: </label>
        <input type="password" name="Password" id="Password" /><br/>
        <input type="hidden" name="Operation" value="LOGIN"/>
        <input type="hidden" name="AccountType" value="JABBER"/>
        <input type="submit" value="Chat on Jabber!" />
      </form>
    </div>
    <?php } else { ?>
    <div class="formbox clientlogin popup" id="JabberDiv">
      <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
            name="logoutform" id="logoutform">
        <input type="hidden" name="Operation" value="DISCONNECT"/>
        <input type="hidden" name="AccountType" value="JABBER"/>
        <input type="submit" value="Logout from Jabber" />
      </form>
    </div>
    <script>
      $("#JABBERUsername").text($.cookie("JABBERUsername"));
    </script>
    <?php } ?>

    <?php if(isset($_COOKIE['UCHATUsername']) && isset($_COOKIE['UCHATSessionId'])) { ?>
    <div class="formbox clientlogin popup" id="UChatDiv">
      <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
            name="logoutform" id="logoutform" >
        <input type="hidden" name="Operation" value="DISCONNECT"/>
        <input type="hidden" name="AccountType" value="UCHAT"/>
        <input type="submit" value="Logout from UXChat" />
      </form>
    </div>
    <script>
      $("#UChatUsername").text($.cookie("UCHATUsername"));
    </script>
    <?php } ?>

  </body>

</html>
