<?php
// Google Cloud Messaging GCM API Key
define("GOOGLE_API_KEY", "YOUR_GCM_API_KEY");

// Config Development mode
define("DEVELOPMENT_MODE", TRUE);

if (DEVELOPMENT_MODE) {
    ini_set('display_errors', 1);
    error_reporting(E_ALL);
}
require_once ('db_connect.php');
require_once ('common.php');

function register() {
    $username = mysql_real_escape_string(trim($_POST['username']));
    $displayName = mysql_real_escape_string(trim($_POST['displayName']));
    $password = trim($_POST['password']);

    // check if empty
    if (empty($username) || empty($displayName) || empty($password)) {
        sendErrorResult(array("message" => "Missing field(s)"));
    }

    // check if username is duiplicated
    $sql = sprintf("SELECT * FROM users WHERE username='%s' LIMIT 1;", $username);
    $result = mysql_query($sql);
    if (mysql_num_rows($result) > 0) {
        // user already exist
        $response = array("message" => "Username already exists");
        sendErrorResult($response);
    }

    // register new user with access token
    $token = sha1(time() . $username . $displayName . md5($password));
    $sql = sprintf("INSERT INTO users (username, display_name, password, token) VALUE ('%s', '%s', '%s', '%s');", $username, $displayName, md5($password), $token);
    if (mysql_query($sql)) {
        $uid = mysql_insert_id();
        $response = array(
            "uid" => $uid,
            "token" => $token
        );
        sendOkResult($response);
    } else {
        sendErrorResult(array("message" => "Unknow Error"));
    }
}

function searchContact() {
    $token = mysql_real_escape_string($_POST['token']);
    
    // Check if token is valid
    $sql = sprintf("SELECT * FROM users WHERE token='%s' LIMIT 1", $token);
    $result = mysql_query($sql);

    if (mysql_num_rows($result) > 0) {
        // FIXME: search for himself
        $username = mysql_real_escape_string($_POST['username']);
        $sql = sprintf("SELECT id AS uid, username, display_name AS displayName FROM users WHERE username='%s' LIMIT 1", $username);
        $result = mysql_query($sql);
        $contact = mysql_fetch_assoc($result);

        $response = array();
        if ($contact)
            $response["contact"] = $contact;

        sendOkResult($response);

    } else {
        sendErrorResult(array("message" => "Not authorized"));
    }

}

function updateGcmRegId() {
    $token = mysql_real_escape_string($_POST['token']);
    
    // Check if token is valid
    $sql = sprintf("SELECT * FROM users WHERE token='%s' LIMIT 1", $token);
    $result = mysql_query($sql);
    
    if (mysql_num_rows($result) > 0) {
        $gcmRegId = mysql_real_escape_string($_POST['gcmRegId']);
        
        // Update user GCM Reg ID
        $sql = sprintf("UPDATE users SET gcm_regid = '%s' WHERE token = '%s'", $gcmRegId, $token);
        $result = mysql_query($sql);

        if ($result) {
            sendOkResult();
        } else {
            sendErrorResult(array('message' => 'Unknow error'));
        }
    } else {
        sendErrorResult(array("message" => "Not authorized"));
    }
}

function sendMessage() {
    $token = mysql_real_escape_string($_POST['token']);
    $uid = mysql_real_escape_string($_POST['uid']);
    $what = $_POST['what'];

    // Check if token is valid
    $sql = sprintf("SELECT * FROM users WHERE token = '%s' LIMIT 1", $token);
    $result = mysql_query($sql);

    if (mysql_num_rows($result) > 0) {
        // Token is valid
        $myself = mysql_fetch_assoc($result);
        $myName = $myself['display_name'];
        $myId = $myself['id'];

        if ($uid == 20) {
            // Hard Code Hack
            $what = array();
            $what[] = "Hi! This is Batman.";
            $what[] = "Batman is protecting this City";
            $what[] = "It’s not who we are underneath, but what we do that defines us.";
            $myGcmId = $myself['gcm_regid'];

            $time = time();
            $choice = $time % 3;

            $gcmResult = sendPushNotificationToGCM($myGcmId, $uid, "Batman", $what[$choice]);
            sendOkResult();
        }

        // Get GCM Reg ID of the user
        $sql = sprintf("SELECT gcm_regid FROM users WHERE id = '%s' LIMIT 1", $uid);
        $result = mysql_query($sql);
        if (mysql_num_rows($result) > 0) {
            $user = mysql_fetch_assoc($result);
            $gcmRegId = $user['gcm_regid'];
            $gcmResult = sendPushNotificationToGCM($gcmRegId, $myId, $myName, $what);
            sendOkResult();
        } else {
            sendErrorResult(array('message' => 'User not found'));
        }
    } else {
        sendErrorResult(array("message" => "Not authorized"));
    }
}

function sendPushNotificationToGCM($regId, $myId, $displayName, $what) {

    $registration_ids = array($regId);

    $data = array(
        "uid" => $myId,
        "display_name" => $displayName,
        "what" => $what
    );
    $message = array("m" => json_encode($data));

    //Google cloud messaging GCM-API url
    $url = 'https://android.googleapis.com/gcm/send';
    $fields = array(
        'registration_ids' => $registration_ids,
        'data' => $message,
    );

    $headers = array(
        'Authorization: key=' . GOOGLE_API_KEY,
        'Content-Type: application/json'
    );
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

    $result = curl_exec($ch);
    if ($result === FALSE) {
        die('Curl failed: ' . curl_error($ch));
    }
    curl_close($ch);
    return $result;
}

$f = $_GET['f'];
call_user_func($f);
?>