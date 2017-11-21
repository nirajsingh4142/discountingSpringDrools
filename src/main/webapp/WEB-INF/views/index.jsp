<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>HBG Discounting</title>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
</head>

<body role="document">
	<!-- Fixed navbar -->
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Home</a></li>

				</ul>
			</div>
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

		<c:choose>
			<c:when test="${!qualifiers.equals(' ')}">
				<div class="alert alert-success" role="alert">
					Rules qualified: <strong><c:out value="${qualifiers}" /></strong>
				</div>
			</c:when>
		</c:choose>
		
		<c:choose>
			<c:when test="${!netOutput.equals(' ')}">
				<div class="alert alert-success" role="alert">
					Output: <strong><c:out value="${netOutput}" /></strong>
				</div>
			</c:when>
		</c:choose>

		<th>
			<form:form method="POST" action="deleteRuleSet"
				commandName="demoForm">
				<div class="input-group">
					<span class="input-group-btn"> <input
						class="btn btn-success" type="submit" value="Reset All" />
					</span>
				</div>
			</form:form>
		</th>

		<!-- RULE SETUP SECTION-->
		<div class="page-header">
			<h1>Add Rules</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form:form method="POST" action="addRule" commandName="demoForm">
					<div class="input-group">
						<form:input path="ruleNumber" id="ruleNumber-input"
							placeholder="Type rule number" class="form-control" />
						<form:input path="ruleName" id="ruleName-input" type="text"
							placeholder="Type rule Name" class="form-control" />
						<form:input path="accountNumber" id="accountNumber-input"
							placeholder="Type account" class="form-control" />
						<form:input path="fc" id="fc-input" type="text"
							placeholder="Type FC" class="form-control" />
						<form:input path="dgp" id="dgp-input" type="text"
							placeholder="Type DGP" class="form-control" />
						<form:input path="accountType" id="accountType-input" type="text"
							placeholder="Type account type" class="form-control" />
						<form:input path="isbn" id="isbn-input" placeholder="Type isbn"
							class="form-control" />
						<form:input path="discount" id="discount-input"
							placeholder="Type discount" class="form-control" />
						<form:input path="priority" id="priority-input" type="text"
							placeholder="Type Priority" class="form-control" />
						<form:input path="terms" id="terms-input" placeholder="Type terms"
							class="form-control" />
						<form:input path="frieghtCharge" id="frieghtCharge-input"
							type="text" placeholder="Type Frieght Charge"
							class="form-control" />
						<form:input path="combo" id="combo-input"
							placeholder="Type New Combo" class="form-control" />
						<form:label path="overridenExplicitly">Overriden Explicitly: <form:checkbox
								path="overridenExplicitly" id="overridenExplicitly-input"
								placeholder="check overriden Explicitly" class="form-control" />
						</form:label>
						<form:label path="hardcode">Hardcode: <form:checkbox
								path="hardcode" id="hardcode-input" placeholder="check harcode"
								class="form-control" />
						</form:label>
						<form:input path="quantityRange1" id="quantityRange1-input"
							placeholder="Type quantity Range1" class="form-control" />
						<form:input path="discountRange1" id="discountRange1-input"
							placeholder="Type discount Range1" class="form-control" />
						<form:input path="quantityRange2" id="quantityRange2-input"
							placeholder="Type quantity Range2" class="form-control" />
						<form:input path="discountRange2" id="discountRange2-input"
							placeholder="Type discount Range2" class="form-control" />

						<span class="input-group-btn"> <input
							class="btn btn-success" type="submit" value="Add Rule" />
						</span>
					</div>
				</form:form>
			</div>
		</div>
		
		<!-- STANDARD RULE SETUP SECTION-->
		<div class="page-header">
			<h1>Add Standard Rules</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form:form method="POST" action="addStandardRule" commandName="demoForm">
					<div class="input-group">
						<form:input path="ruleNumber" id="ruleNumber-input"
							placeholder="Type rule number" class="form-control" />
						<form:input path="ruleName" id="ruleName-input" type="text"
							placeholder="Type rule Name" class="form-control" />
						<form:input path="accountType" id="accountType-input" type="text"
							placeholder="Type account type" class="form-control" />
						<form:input path="fc" id="fc-input" type="text"
							placeholder="Type FC" class="form-control" />
						<form:input path="isbn" id="isbn-input" placeholder="Type isbn"
							class="form-control" />
						<form:input path="quantityRange1" id="quantityRange1-input"
							placeholder="Type quantity Range1" class="form-control" />
						<form:input path="discountRange1" id="discountRange1-input"
							placeholder="Type discount Range1" class="form-control" />
						<form:input path="quantityRange2" id="quantityRange2-input"
							placeholder="Type quantity Range2" class="form-control" />
						<form:input path="discountRange2" id="discountRange2-input"
							placeholder="Type discount Range2" class="form-control" />
						<form:input path="quantityRange3" id="quantityRange3-input"
							placeholder="Type quantity Range3" class="form-control" />
						<form:input path="discountRange3" id="discountRange3-input"
							placeholder="Type discount Range3" class="form-control" />

						<span class="input-group-btn"> <input
							class="btn btn-success" type="submit" value="Add Standard Rule" />
						</span>
					</div>
				</form:form>
			</div>
		</div>
		
		<!-- ORDER SETUP SECTION-->
		<div class="page-header">
			<h1>Add Order Lines</h1>
		</div>
		<div class="row">
			<div class="col-md-6">
				<form:form method="POST" action="addOrder" commandName="demoForm">
					<div class="input-group">
						<form:input path="orderLineNumber" id="orderLineNumber-input"
							placeholder="Type order line number" class="form-control" />
						<form:input path="accountNumber" id="accountNumber-input"
							placeholder="Type account" class="form-control" />
						<form:input path="accountType" id="accountType-input" 
							placeholder="Type account type" class="form-control" />
						<form:input path="fc" id="fc-input" type="text"
							placeholder="Type FC" class="form-control" />
						<form:input path="dgp" id="dgp-input" type="text"
							placeholder="Type DGP" class="form-control" />
						<form:input path="isbn" id="isbn-input" placeholder="Type isbn"
							class="form-control" />
						<form:input path="quantity" id="quantity-input"
							placeholder="Type quantity" class="form-control" />

						<span class="input-group-btn"> <input
							class="btn btn-success" type="submit" value="Add Order" />
						</span>
					</div>
				</form:form>
			</div>
		</div>

		<!-- Added Rules are displayed below -->

		<ul class="nav nav-tabs" role="tablist">
			<li class="active"><a href="#room" role="tab" data-toggle="tab">Rule
					Set</a></li>
			<li><a href="#orderLine" role="tab" data-toggle="tab">Order
					Lines</a></li>
			<li><a href="#standardRule" role="tab" data-toggle="tab">Standard Rule</a></li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content">
			<div class="tab-pane active" id="room">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Rule Number</th>
									<th>Rule Name</th>
									<th>Account</th>
									<th>FC</th>
									<th>DGP</th>
									<th>AT</th>
									<th>Isbn</th>
									<th>Discount</th>
									<th>Priority</th>
									<th>Terms</th>
									<th>Frieght Charge</th>
									<th>Combo</th>
									<th>Overriden Explicitly</th>
									<th>Hardcode</th>
									<th>Quantity Range1</th>
									<th>Discount Range1</th>
									<th>Quantity Range2</th>
									<th>Discount Range2</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="sprinkler" varStatus="item"
									items="${sprinklers}">
									<tr>
										<td>${sprinkler.ruleSetup.ruleNumber}</td>
										<td>${sprinkler.ruleSetup.ruleName}</td>
										<td>${sprinkler.ruleSetup.account.accountNumber}</td>
										<td>${sprinkler.ruleSetup.product.familyCode}</td>
										<td>${sprinkler.ruleSetup.product.productGroupCode}</td>
										<td>${sprinkler.ruleSetup.account.accountType}</td>
										<td>${sprinkler.ruleSetup.product.isbn}</td>
										<td>${sprinkler.ruleSetup.discount.percentage}</td>
										<td>${sprinkler.ruleSetup.offer.priority}</td>
										<td>${sprinkler.ruleSetup.offer.days}</td>
										<td>${sprinkler.ruleSetup.offer.frieghtCharge}</td>
										<td>${sprinkler.ruleSetup.offer.comboField}</td>
										<td>${sprinkler.ruleSetup.offer.overridenExplicitly}</td>
										<td>${sprinkler.ruleSetup.offer.hardcode}</td>
										<td>${sprinkler.ruleSetup.quantityRange1}</td>
										<td>${sprinkler.ruleSetup.discountRange1}</td>
										<td>${sprinkler.ruleSetup.quantityRange2}</td>
										<td>${sprinkler.ruleSetup.discountRange2}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			
			<div class="tab-pane" id="standardRule">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Rule Number</th>
									<th>Rule Name</th>
									<th>Account Type</th>
									<th>FC</th>
									<th>Isbn</th>
									<th>Quantity Range1</th>
									<th>Discount Range1</th>
									<th>Quantity Range2</th>
									<th>Discount Range2</th>
									<th>Quantity Range3</th>
									<th>Discount Range3</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="standardSprinkler" varStatus="item"
									items="${standardSprinklers}">
									<tr>
										<td>${standardSprinkler.standardRule.ruleNumber}</td>
										<td>${standardSprinkler.standardRule.ruleName}</td>
										<td>${standardSprinkler.standardRule.account.accountType}</td>
										<td>${standardSprinkler.standardRule.product.familyCode}</td>
										<td>${standardSprinkler.standardRule.product.isbn}</td>
										<td>${standardSprinkler.standardRule.quantityRange1}</td>
										<td>${standardSprinkler.standardRule.discountRange1}</td>
										<td>${standardSprinkler.standardRule.quantityRange2}</td>
										<td>${standardSprinkler.standardRule.discountRange2}</td>
										<td>${standardSprinkler.standardRule.quantityRange3}</td>
										<td>${standardSprinkler.standardRule.discountRange3}</td>
									</tr>
								</c:forEach>
							</tbody> 
						</table>
					</div>
				</div>
			</div>

			<div class="tab-pane" id="orderLine">
				<div class="row">
					<div class="col-md-12">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>Order Line#</th>
									<th>Account Number</th>
									<th>AT</th>
									<th>FC</th>
									<th>DGP</th>
									<th>Isbn</th>
									<th>Quantity</th>
									<th>
										<form:form method="POST" action="generateOffer"
											commandName="demoForm">
											<div class="input-group">
												<span class="input-group-btn"> <input
													class="btn btn-success" type="submit" value="Generate Offer" />
												</span>
											</div>
										</form:form>
									</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="orderSprinkler" varStatus="item"
									items="${orderSprinklers}">
									<tr>
										<form:form method="POST" action="deleteOrder" commandName="demoForm">
											<td>${orderSprinkler.orderLine.orderLineId}</td>
											<td>${orderSprinkler.orderLine.account.accountNumber}</td>
											<td>${orderSprinkler.orderLine.account.accountType}</td>
											<td>${orderSprinkler.orderLine.product.familyCode}</td>
											<td>${orderSprinkler.orderLine.product.productGroupCode}</td>
											<td>${orderSprinkler.orderLine.product.isbn}</td>
											<td>${orderSprinkler.orderLine.quantity}</td>
											<td><span class="input-group-btn"> <input class="btn btn-success" type="submit" value="Delete Order" />
											</span></td>
										</form:form>
									</tr>
								</c:forEach>

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
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</body>
</html>