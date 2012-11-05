<?php

function redirectTohttps() {
   if($_SERVER['HTTP_X_FORWARDED_PORT'] != "443") {
       $redirect= "https://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'];
       header("Location:$redirect");
   }
}

function handleCases($operation, $result) {
   
   if(preg_match('/GETFRIENDS/i', $operation)) {
       $logins = explode(',', $result);
        $retVal = array();
        foreach($logins as $val) {
            if(preg_match('/Username: /i', $val)) {
                $user = preg_replace('/Username:/i', '', $val);
                $user = preg_replace('/ /', '', $user);
                $retVal[] = $user;
            }
        }
        return $retVal;
   } else if(preg_match('/VALIDATE/i', $operation)) {
        if(!preg_match('/Success/i', $result)) {
            die("Don't try to hack my system :(");
        } 
   } 
   return $result;
}

function doProxy($args) {
        $POSTURL = 'https://cryptic-plateau-9733.herokuapp.com//islab/InfoSecLab';

        if(isset($args)) {
            $query = $args;
        } else {
            $query = $_SERVER['QUERY_STRING'];
        }

        $cookie = array();
        foreach( $_COOKIE as $key => $value ) {
            $cookie[] = "{$key}={$value}";
        };

        $cookie = implode('; ', $cookie);
 
        $ch = curl_init($POSTURL);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $query);
        curl_setopt($ch, CURLOPT_COOKIE, $cookie);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, 1);
        curl_setopt($ch, CURLOPT_HEADER, 0);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_AUTOREFERER, 1);
        $result = htmlspecialchars(curl_exec($ch));
        $retval = handleCases($query, $result);
        return $retval;
}

?>
