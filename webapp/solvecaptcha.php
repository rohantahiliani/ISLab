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
        <div class="formbox" id="logindiv">

          <form method="POST" action="https://cryptic-plateau-9733.herokuapp.com/islab/InfoSecLab" 
                name="registerform" id="registerform">
            <?php
               require_once("recaptchalib.php");
               $publickey = "6LcksdgSAAAAAMw0IiovyZMDjGhEen6ZSFNZeAqw";
               echo recaptcha_get_html($publickey, null, true);
               ?>
            <input type="submit" value="Solve!" />
          </form>
        </div>
      </div>
    </div>
  </body>
</html>
