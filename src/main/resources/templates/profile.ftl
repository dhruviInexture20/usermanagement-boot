
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome Page</title>

<link rel="stylesheet" href="/library/bootstrap/css/bootstrap.min.css" >

<link rel="stylesheet" href="/css/welcome.css" >
<script src= "/library/jquery/jquery-3.6.0.min.js" > </script>
<script src= "/library/bootstrap/js/bootstrap.min.js" > </script>
</head>

<body>


<div class="container emp-profile">
	        
                <div class="row">
                    <div class="col-md-4">
                        <div class="profile-img">
                            <img src="data:image/jpg;base64,<#if user?? & user.profilepic??>${user.profilepic}</#if>" alt=""/>
                        </div>
        
                    </div>
                    <div class="col-md-6">
                        <div class="profile-head">
                                    <h4>
                                       <#if user?? && user.firstname??>${ user.firstname }</#if> <#if user?? && user.lastname??>${ user.lastname }</#if>
                                        
                                    </h4>
                                    <h6>
                                       <#if user?? && user.designation??>${ user.designation }</#if>
                                        
                                    </h6>
                                    
                            <ul class="nav nav-tabs" id="myTab" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">About</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <!-- <a href="registration.jsp" class="profile-edit-btn"  >Edit Profile </a> -->
                        
                        <a href="logOutController" class="btn btn-primary text-center">Logout</a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-4 text-center">
                    <div class="row">
                    	<form action="editUserController" method="post">
                        	<input type="hidden" value="<#if user?? && user.userid??>${user.userid}</#if>" id="hidden_userid" name="userid">
            				<input type="submit" class="btn btn-primary" value="Edit Profile" id="submit">
                        
                        </form>
                    	
                    </div>            
               
                    </div>
                    <div class="col-md-8">
                        <div class="tab-content profile-tab" id="myTabContent">
                            <div class="tab-pane fade show active in" id="home" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>User Id</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.userid??>${user.userid}</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Name</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.firstname??>${ user.firstname }</#if> <#if user?? && user.lastname??>${ user.lastname }</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Email</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.email??>${ user.email }</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Phone</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.phone??>${ user.phone }</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Designation</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.designation??>${ user.designation }</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Gender</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.gender??>${ user.gender }</#if></p>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6">
                                                <label>Date of Birth</label>
                                            </div>
                                            <div class="col-md-6">
                                                <p><#if user?? && user.birthdate??>${ user.birthdate }</#if></p>
                                            </div>
                                        </div>
                            </div>
                        </div>
                   
                    </div>
                </div>
        </div>



</body>
</html>