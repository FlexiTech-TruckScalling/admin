/**
 * 
 */
$(() => {
	const validationClasses = [
		'required',
		'pattern[password]',
		'pattern[email]',
		'pattern[number]',
		'pattern[dd/mm/yyyy]',
		'pattern[dd/mm/yy hh:mm:ss]'
	];

	$(document).on("submit", "#common-form", function(event) {
		console.log("Form click!");

		event.preventDefault();

		let isValid = true;

		$("#common-form input").each(function() {
			const $input = $(this);
			const $next = $(this).next();
			const classes = $input.attr("class") ? $input.attr("class").split(" ") : [];
			const errorMsgObj = { msg: "", prefix: "This Field" };
			for (let vClass of validationClasses) {
				if (classes.includes(vClass)) {
					let prefix = $next.data("label");
					errorMsgObj.prefix = prefix;
					if (!validateInput($input, vClass, errorMsgObj)) {
						$next.removeClass('input-error-msg');
						$input.addClass('border border-danger')
						$next.text(errorMsgObj.msg)
						isValid = false;
					} else {
						$input.removeClass('border border-danger')
						$next.addClass('input-error-msg');
						$next.text('')
					}
				}
			}
		});

		$("#common-form select.selectpicker").each(function() {
			const $input = $(this);
			const $next = $(this.parentElement).next();
			const classes = $input.attr("class") ? $input.attr("class").split(" ") : [];
			const errorMsgObj = { msg: "", prefix: "This Field" };
			for (let vClass of validationClasses) {
				if (classes.includes(vClass)) {
					let prefix = $next.data("label");
					errorMsgObj.prefix = prefix;
					if (!validateInput($input, vClass, errorMsgObj)) {
						$next.removeClass('input-error-msg');
						$input.parent().addClass('border border-danger')
						$next.text(errorMsgObj.msg)
						isValid = false;
					} else {
						$input.parent().removeClass('border border-danger')
						$next.addClass('input-error-msg');
						$next.text('')
					}
				}
			}
		});

		// If the form is valid, submit the form
		if (isValid) {
			console.log("Form is valid!");
			// Optionally trigger form submission
			var page = document.getElementById("pageNo");
			if(page){
				$('#pageNo').val(1);
			}
			this.submit();
		} else {
			console.log("Form is invalid!");
		}
	});

	// Function to handle input validation based on the class
	function validateInput($input, validationClass, errorMsgObj) {
		let value = $input.val();
		if(value){
			value = value.trim();
		}

		// Required field validation
		if (validationClass === "required") {
			if (!value) {
				errorMsgObj.msg = errorMsgObj.prefix + " is required!";
				$input.focus();
				return false;
			}
		}

		// Pattern validation for password
		if (validationClass === "pattern[password]") {
			const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; // At least 8 chars, 1 letter, 1 number
			if (!passwordRegex.test(value)) {
				errorMsgObj.msg = "Password must be at least 8 characters, with at least one letter and one number.";
				$input.focus();
				return false;
			}
		}

		// Pattern validation for email
		if (validationClass === "pattern[email]") {
			const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
			if (!emailRegex.test(value)) {
				errorMsgObj.msg = "Please enter a valid email address.";
				$input.focus();
				return false;
			}
		}

		// Pattern validation for number
		if (validationClass === "pattern[number]") {
			const numberRegex = /^\d+$/;
			if (!numberRegex.test(value)) {
				errorMsgObj.msg = "Please enter a valid number.";
				$input.focus();
				return false;
			}
		}

		// Pattern validation for date (dd/mm/yyyy)
		if (validationClass === "pattern[dd/mm/yyyy]") {
			const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/;
			if (!dateRegex.test(value)) {
				errorMsgObj.msg = "Please enter a valid date in dd/mm/yyyy format.";
				$input.focus();
				return false;
			}
		}

		// Pattern validation for date and time (dd/mm/yy hh:mm:ss)
		if (validationClass === "pattern[dd/mm/yy hh:mm:ss]") {
			const dateTimeRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{2} ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
			if (!dateTimeRegex.test(value)) {
				errorMsgObj.msg = "Please enter a valid date and time in dd/mm/yy hh:mm:ss format.";
				$input.focus();
				return false;
			}
		}

		// If all validations pass
		return true;
	}


})