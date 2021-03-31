<?php
   require 'standard/connect.php';

   $grating = mysqli_real_escape_string($con, $_POST['grating']);
   $diameter = mysqli_real_escape_string($con, $_POST['diameter']);
   $result = mysqli_query($con,"SELECT pname FROM products WHERE grating='$grating' and diameter='$diameter'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

if ($data) { 
 echo $data;
}

 else {
 	echo "No Meter Found";
}

  mysqli_close($con);
?>