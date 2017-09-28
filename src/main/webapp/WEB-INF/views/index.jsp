<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Demo-Home</title>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body role="document">
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">OTC Discounting App</a>
				
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>
					
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
	<div class="container theme-showcase" role="main">
		<div class="page-header">
			<h1>
				<!-- Intentionally left blank to give some leading space -->
			</h1>
		</div>
		<div class="page-header">
			<h2>HBG Discounting Application</h2>
		</div>
		<h3><!-- The below demo shows if there is any fire in the configured rooms --></h3>
		<c:if test="${isError ==true}">
			<div class="alert alert-danger" role="alert">
				Oh snap!
				<c:out value="${error}" />
				.
			</div>
		</c:if>

		<c:choose>
			<c:when test="${alarmsFound == true}">
				<div class="alert alert-danger" role="alert">
					Oh snap! There are <strong><c:out value="${noOfAlarms}" /></strong> alarms raised.
				</div>
			</c:when>

			<c:otherwise>
				<div class="alert alert-success" role="alert">
					There are <strong>no</strong> alarms raised.
				</div>
			</c:otherwise>
		</c:choose>

		<h3>
			<span class="label label-default">The time on the server is ${serverTime}.</span>
		</h3>
		<div class="page-header">
			<h1>Add Rules</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form:form method="POST" action="addroom" commandName="demoForm">
					<div class="input-group">
						<form:input path="ruleNumber" id="ruleNumber-input" placeholder="Type rule number" class="form-control" />
						<form:input path="ruleName" id="ruleName-input" type="text" placeholder="Type rule Name" class="form-control" />
						<form:input path="accountNumber" id="accountNumber-input" placeholder="Type account" class="form-control" />
						<form:input path="fc" id="fc-input" type="text" placeholder="Type FC" class="form-control" />
						<form:input path="dgp" id="dgp-input" type="text" placeholder="Type DGP" class="form-control" />
						<form:input path="accountType" id="accountType-input" type="text" placeholder="Type account type" class="form-control" />
						<form:input path="isbn" id="isbn-input" placeholder="Type isbn" class="form-control" />
						<form:input path="discount" id="discount-input" placeholder="Type discount" class="form-control" />
						<form:input path="terms" id="terms-input" placeholder="Type terms" class="form-control" />
						<form:input path="priority" id="priority-input" type="text" placeholder="Type Priority" class="form-control" />
						<form:input path="combo" id="combo-input" placeholder="Type New Combo" class="form-control" />
						<form:label path = "overridenExplicitly">Overriden Explicitly: <form:checkbox path="overridenExplicitly" id="overridenExplicitly-input" placeholder="check overriden Explicitly" class="form-control" /></form:label>
						<form:label path = "hardcode">Hardcode: <form:checkbox path="hardcode" id="hardcode-input" placeholder="check harcode" class="form-control" /></form:label>
						
						
						<span class="input-group-btn"> <input class="btn btn-success" type="submit" value="Add Rule" />
						</span>
					</div>
				</form:form>
			</div>
			
			<div class="col-md-12">
				<a class="btn btn-info col-md-12" href="./">Refresh Status</a>
			</div>
		</div>

		<ul class="nav nav-tabs" role="tablist">
			<li class="active"><a href="#room" role="tab" data-toggle="tab">Rule Set</a></li>
		</ul>
		<!-- Tab panes -->
		<div class="tab-content">
			<div class="tab-pane active" id="room">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>#</th>
									<th>Rule Number</th>
									<th>Rule Name</th>
									<th>Account</th>
									<th>FC</th>
									<th>DGP</th>
									<th>AT</th>
									<th>Isbn</th>
									<th>Discount</th>
									<th>Terms</th>
									<th>Priority</th>
									<th>Combo</th>
									<th>Overriden Explicitly</th>
									<th>Hardcode</th>
									
								</tr>
							</thead>
							<tbody>

								<c:forEach var="sprinkler" varStatus="item" items="${sprinklers}">

									<tr>
										<td>${item.index + 1}</td>
										<td>${sprinkler.ruleSetup.ruleNumber}</td>
										<td>${sprinkler.ruleSetup.ruleName}</td>
										
										<td><c:choose>
												<c:when test="${sprinkler.on}">
													<span class="label label-danger">On Fire</span>
												</c:when>

												<c:otherwise>
													<span class="label label-success">Normal</span>
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${sprinkler.on}">
													<span class="label label-success">On</span>
												</c:when>

												<c:otherwise>
													<span class="label label-default">Off</span>
												</c:otherwise>
											</c:choose></td>
											
									</tr>
								</c:forEach>



							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="tab-pane" id="alarm">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>#</th>
									<th>Room Name</th>
									<th>Status</th>
									<th>Clear Fire</th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${alarmsFound == true}">

									<c:forEach var="alarm" varStatus="item" items="${alarms}">
										<tr>
											<td>${item.index + 1}</td>
											<td>${alarm.room.name}</td>
											<td><span class="label label-danger">On Fire</span></td>
											<td><form:form method="POST" action="remfire" commandName="demoForm">
													<div class="input-group">
														<form:hidden path="fireRoomName" value="${alarm.room.name}" />
														<input class="btn btn-success" type="submit" value="Put off Fire" />

													</div>
												</form:form></td>
										</tr>
									</c:forEach>
								</c:if>

							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->



	<!-- Latest compiled and minified Jquery and Bootstrap JavaScript -->
	<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</body>
</html>