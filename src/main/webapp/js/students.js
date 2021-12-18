const studentBaseUrl = "http://localhost:8080/students";

function renderStudentsList() {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: studentBaseUrl,
    })
        .done((response) => {
            renderStudentList(response);
            $("#student-list").show();
        })
        .fail((response) => {
            const $errorAlert = $("#error-alert");
            $errorAlert.text(response.message);
            $errorAlert.removeClass("d-none");
        })
        .always(() => {
            $("#student-loading").addClass("d-none").removeClass("d-flex");
        });

    $("#student-list").hide();
    $("#error-alert").addClass("d-none");
    $("#student-loading").addClass("d-flex").removeClass("d-none");
}

function createStudent() {
    $.ajax({
        url: studentBaseUrl,
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getStudentFormFieldsJson()),
        dataType: 'json',
        statusCode: {
            200: handle200,
            201: handle201,
            404: handle404
        }
    })
        .done(() => {
            window.location.replace("./students.html");
        })
        .fail((response) => {});
}

function editStudent() {
    $.ajax({
        url: `${studentBaseUrl}`,
        method: "PUT",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(getStudentFormFieldsJson()),
        dataType: 'json',
        statusCode: {
            200: handle200,
            201: handle201,
            404: handle404
        }
    })
        .done(() => {
            window.location.replace("./students.html");
        })
        .fail((response) => {});
}

function deleteStudent(id) {
    $.ajax({
        url: `${studentBaseUrl}/${id}`,
        method: "DELETE",
        success: function () {
            renderStudentsList();
        },
    });
}

function renderStudentList(students = []) {
    $studentList = $("#student-list");
    $studentList.empty();

    students.forEach((student) => {
        const $template = getStudentTemplate(student);
        $studentList.append($template);
    });
}

function getStudentTemplate(student) {
    const templateSelector = `#student-list-template`;
    const $template = $($(templateSelector).html());
    renderCoursesForStudent($template, student.id);
    $template
        .find(".student-name")
        .text(`${student.firstName} ${student.lastName}`);

    $template.find("#delete-student").click(() => deleteStudent(student.id));
    $template
        .find("#edit-student")
        .click(() =>
            window.location.replace(`./student-edit.html?id=${student.id}`)
        );
    return $template;
}

function getStudentFormFieldsJson() {
    const id = $("#student-id").val();
    const firstName = $("#firstName").val();
    const lastName = $("#lastName").val();
    const courseIds = $("#courses-select")
        .val()
        .filter((a) => a);

    return {"id": id, "firstName": firstName, "lastName": lastName, "courseIds": courseIds};
}

function fillFormForStudent(id) {
    $.ajax({
        method: "GET",
        headers: {'Content-Type': 'application/json'},
        url: `${studentBaseUrl}/${id}`,
    }).done((response) => {
        $("#student-id").val(response.id);
        $("#firstName").val(response.firstName);
        $("#lastName").val(response.lastName);
        $("#courses-select").val(response.courseIds);
    });
}

const handle200 = function (data, textStatus, jqXHR) {
    window.location.replace("./students.html");
};

const handle201 = function (data, textStatus, jqXHR) {
    window.location.replace("./students.html");
};

const handle404 = function (jqXHR, textStatus, errorThrown) {
    alert(errorThrown);
};
