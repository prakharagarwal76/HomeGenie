<?php
//$servername = "192.168.1.34";
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "automaticirrigation";


$conn =  new mysqli($servername, $username, $password, $dbname);
if(mysqli_connect_error())
    {
      echo"error".mysqli_connect_error();
      exit();
    }
// Check connection
if ($conn) {

  //echo "status/0";

// $conn->select_db($dbname);
$sql = "SELECT * FROM MASTERCONTROL";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        if($row["status"]==0)
          {
            $hdr=array();
    
  $hdr['status']="status0";
  
  $response=$hdr;

    header("Content-type: application/json");
    
  echo json_encode($response);
          
          }
          else if($row["status"]==1)
            {
             $hdr=array();
    
  $hdr['status']="status1";
  
  $response=$hdr;

    header("Content-type: application/json");
    
  echo json_encode($response);
            }
    }
}

$conn->close();
}
?>
