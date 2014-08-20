<?php

function sendErrorResult($result = null) {
    http_response_code(400);
    $response = array('result' => FALSE);
    if ($result != null)
        $response = array_merge($response, $result);
    echo json_encode($response);
    exit ;
}

function sendOkResult($result = null) {
    $response = array('result' => TRUE);
    if ($result != null)
        $response = array_merge($response, $result);
    echo json_encode($response);
    exit ;
}
