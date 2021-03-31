<?php
//	type 1: return single-row result
   require 'standard/connect.php';

   $dname = mysqli_real_escape_string($con, $_POST['dname']);

   $sql = "SELECT location FROM files WHERE dname='$dname'";
   $result = mysqli_query($con, $sql);  
   $row = mysqli_fetch_array($result);
   $data = $row[0];

if ($data) { 
 echo $data;
}
else {
 echo "No files Found";
}

   mysqli_close($con);
?>