let baseUrl = "http://localhost:8080/courses";

function getCourses() {
    $.ajax({
        method: "GET",
        url: baseUrl,
        headers: {'Content-Type': 'application/json'},
        data: getCurrentFilters(),
    })
        .done((response) => {
            renderCourseList(response);
            $("#course-list").show();
        })
        .fail((response) => {
            const $errorAlert = $("#error-alert");
            $errorAlert.text(response.message);
            $errorAlert.removeClass("d-none");
        })
        .always(() => {
            $("#course-loading").addClass("d-none").removeClass("d-flex");
        });

    $("#course-list").hide();
    $("#error-alert").addClass("d-none");
    $("#course-loading").addClass("d-flex").removeClass("d-none");
}

function createCourse() {
    $.ajax({
        url: baseUrl,
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getCourseFormFieldsJSON()),
        dataType: 'json',
        statusCode: {
            200: handle200Res,
            201: handle201Res,
            404: handle404Res
        }
    })
        .done(() => {
            window.location.replace("./index.html");
        })
        .fail((response) => {
        });
}

function editCourse() {
    $.ajax({
        url: `${baseUrl}`,
        method: "PUT",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getCourseFormFieldsJSON()),
        dataType: 'json',
        statusCode: {
            200: handle200Res,
            201: handle201Res,
            404: handle404Res
        }
    })
        .done(() => {
            window.location.replace("./index.html");
        })
        .fail((response) => {
        });
}


function deleteCourse(id) {
    $.ajax({
        url: `${baseUrl}/${id}`,
        method: "DELETE",
        success: function () {
            getCourses();
        },
    });
}

function renderCourseList(courses = []) {
    $courseList = $("#course-list");
    $courseList.empty();

    courses.forEach((course) => {
        const $template = getCourseTemplate(course);
        $courseList.append($template);
    });
}

function getCourseTemplate(course) {
    const templateSelector = `#course-list-template`;
    const $template = $($(templateSelector).html());
    course.teacherId && renderTeacherForCourse($template, course.teacherId);
    $template.find(".course-title").text(course.title);

    $template.find(".course-price").text(course.price);
    $template.find("#delete-course").click(() => deleteCourse(course.id));
    $template
        .find("#edit-course")
        .click(() => window.location.replace(`./courses-edit.html?id=${course.id}`));
    return $template;
}

function getCurrentFilters() {
    const teacherId = $("#teacher-select").val();
    const priceGreaterThan = $("#price-more").val();
    const priceLessThan = $("#price-less").val();

    const filterObject = {teacherId, priceGreaterThan, priceLessThan};
    Object.keys(filterObject).forEach(
        (key) => filterObject[key] === "" && delete filterObject[key]
    );

    return filterObject;
}

function getCourseFormFieldsJSON() {
    const id = $("#course-id").val();
    const title = $("#title").val();
    const price = $("#price").val();
    const teacherId = $("#teacher-select").val();

    return {"id": id, "title": title, "price": price, "teacherId": teacherId};
}

function renderTeacherForCourse($template, teacherId) {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `http://localhost:8080/teachers/${teacherId}`,
    }).done((response) => {
        $template
            .find("#course-teacher-name")
            .text(`${response.firstName} ${response.lastName}`);
    });
}

function fillFormForCourse(id) {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `${baseUrl}/${id}`,
    }).done((response) => {
        $("#course-id").val(response.id);
        $("#title").val(response.title);
        $("#price").val(response.price);
        $("#teacher-select").val(response.teacherId);
    });
}

function getCoursesOptions() {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `${baseUrl}/select`,
    }).done((response = []) => {
        const teacherSelect = $("#courses-select");
        response.forEach((selectOption) =>
            teacherSelect.append(
                `<option value="${selectOption.id}">${selectOption.title}</option>`
            )
        );
    });
}

function renderCoursesForStudent($template, studentId) {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `${baseUrl}/students/${studentId}`,
    }).done((response = []) => {
        const studentSelect = $template.find(".attended-course-list");
        response.forEach((course) =>
            studentSelect.append(`<li class="list-group-item">${course.title}</li>`)
        );
    });
}

const handle200Res = function (data, textStatus, jqXHR) {
    window.location.replace("./index.html");
};

const handle201Res = function (data, textStatus, jqXHR) {
    window.location.replace("./index.html");
};

const handle404Res = function (jqXHR, textStatus, errorThrown) {
    alert(errorThrown);
};
