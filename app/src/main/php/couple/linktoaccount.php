<?php

$con = new mysqli("localhost","deb8634n5_gfo","LWU7i0LQ","deb8634n5_gfo");
 
if ($con->connect_error) {
   die("Connection failed: " . $con->connect_error);
}

$account = mysqli_real_escape_string($con, $_POST['account']);
$tocouple = json_decode($_POST['tocouple']);
$touncouple = json_decode($_POST['touncouple']);

foreach ($tocouple as $entry) {
   $sql = "INSERT INTO couple(username, product) VALUES('$account', '$entry')";
   $con->query($sql);
}

foreach ($touncouple as $entry) {
   $sql = "DELETE FROM couple WHERE (username='$account' AND product='$entry')";
   $con->query($sql);
}

echo "Session ended";

mysqli_close($con);

?>