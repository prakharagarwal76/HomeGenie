<?php
//$servername = "192.168.1.34";
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "automaticirrigation";

$conn = new mysqli($servername, $username, $password, $dbname);
if(mysqli_connect_error())
    {
      echo"error".mysqli_connect_error();
      exit();
    }

else{
	$str = file_get_contents('php://input');
	$json = json_decode($str, TRUE); // decode the JSON into an associative array
   $status = $json['data']['status'];
$sql = "UPDATE MASTERCONTROL SET status=".$status.";";

if ($conn->query($sql) === TRUE) {
	$hdr=array();
    
  $hdr['status']=$status;
  
  $response=$hdr;

    header("Content-type: application/json");
    
  echo json_encode($response);
    
} else {
    $hdr=array();
    
  $hdr['status']="not updated";
  
  $response=$hdr;

    header("Content-type: application/json");
    
  echo json_encode($response);
}

}
?>
