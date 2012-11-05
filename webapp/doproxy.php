<?php
if($_SERVER['REQUEST_METHOD']==='GET') {
   require_once('functions.php');
   echo doProxy(NULL);
} else {
   die('Dont hack my server please :(');
}
?>
