# DefaultApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**adminStatisticsCourseCourseIdGet**](DefaultApi.md#adminStatisticsCourseCourseIdGet) | **GET** /admin/statistics/course/{courseId} |  |
| [**adminStatisticsOverallGet**](DefaultApi.md#adminStatisticsOverallGet) | **GET** /admin/statistics/overall |  |
| [**adminStatisticsStudentsCourseCourseIdGet**](DefaultApi.md#adminStatisticsStudentsCourseCourseIdGet) | **GET** /admin/statistics/students/course/{courseId} |  |
| [**adminStatisticsStudentsOverallGet**](DefaultApi.md#adminStatisticsStudentsOverallGet) | **GET** /admin/statistics/students/overall |  |
| [**adminUsersCountGet**](DefaultApi.md#adminUsersCountGet) | **GET** /admin/users/count |  |
| [**adminUsersGet**](DefaultApi.md#adminUsersGet) | **GET** /admin/users |  |
| [**adminUsersStaffPost**](DefaultApi.md#adminUsersStaffPost) | **POST** /admin/users/staff |  |
| [**adminUsersUserIdGet**](DefaultApi.md#adminUsersUserIdGet) | **GET** /admin/users/{userId} |  |
| [**adminUsersUserIdStatusPut**](DefaultApi.md#adminUsersUserIdStatusPut) | **PUT** /admin/users/{userId}/status |  |
| [**authAdminCreateInitialPost**](DefaultApi.md#authAdminCreateInitialPost) | **POST** /auth/admin/create-initial |  |
| [**authLoginPost**](DefaultApi.md#authLoginPost) | **POST** /auth/login |  |
| [**authLogoutPost**](DefaultApi.md#authLogoutPost) | **POST** /auth/logout |  |
| [**authMeGet**](DefaultApi.md#authMeGet) | **GET** /auth/me |  |
| [**authRefreshPost**](DefaultApi.md#authRefreshPost) | **POST** /auth/refresh |  |
| [**authStaffRegisterPost**](DefaultApi.md#authStaffRegisterPost) | **POST** /auth/staff/register |  |
| [**authStudentRegisterPost**](DefaultApi.md#authStudentRegisterPost) | **POST** /auth/student/register |  |
| [**contentCoursesCourseIdDelete**](DefaultApi.md#contentCoursesCourseIdDelete) | **DELETE** /content/courses/{courseId} |  |
| [**contentCoursesCourseIdGet**](DefaultApi.md#contentCoursesCourseIdGet) | **GET** /content/courses/{courseId} |  |
| [**contentCoursesCourseIdPut**](DefaultApi.md#contentCoursesCourseIdPut) | **PUT** /content/courses/{courseId} |  |
| [**contentCoursesGet**](DefaultApi.md#contentCoursesGet) | **GET** /content/courses |  |
| [**contentCoursesPost**](DefaultApi.md#contentCoursesPost) | **POST** /content/courses |  |
| [**contentReportsGet**](DefaultApi.md#contentReportsGet) | **GET** /content/reports |  |
| [**contentReportsPost**](DefaultApi.md#contentReportsPost) | **POST** /content/reports |  |
| [**contentReportsReportIdGet**](DefaultApi.md#contentReportsReportIdGet) | **GET** /content/reports/{reportId} |  |
| [**contentSectionsByCourseCourseIdGet**](DefaultApi.md#contentSectionsByCourseCourseIdGet) | **GET** /content/sections/by-course/{courseId} |  |
| [**contentSectionsPost**](DefaultApi.md#contentSectionsPost) | **POST** /content/sections |  |
| [**contentSectionsSectionIdDelete**](DefaultApi.md#contentSectionsSectionIdDelete) | **DELETE** /content/sections/{sectionId} |  |
| [**contentSectionsSectionIdGet**](DefaultApi.md#contentSectionsSectionIdGet) | **GET** /content/sections/{sectionId} |  |
| [**contentSectionsSectionIdPut**](DefaultApi.md#contentSectionsSectionIdPut) | **PUT** /content/sections/{sectionId} |  |
| [**contentStatsCourseCourseIdTasksCountGet**](DefaultApi.md#contentStatsCourseCourseIdTasksCountGet) | **GET** /content/stats/course/{courseId}/tasks-count |  |
| [**contentStatsGet**](DefaultApi.md#contentStatsGet) | **GET** /content/stats |  |
| [**contentStatsSectionSectionIdTasksCountGet**](DefaultApi.md#contentStatsSectionSectionIdTasksCountGet) | **GET** /content/stats/section/{sectionId}/tasks-count |  |
| [**contentStatsThemeThemeIdTasksCountGet**](DefaultApi.md#contentStatsThemeThemeIdTasksCountGet) | **GET** /content/stats/theme/{themeId}/tasks-count |  |
| [**contentTasksPost**](DefaultApi.md#contentTasksPost) | **POST** /content/tasks |  |
| [**contentTasksTaskIdAnswerDelete**](DefaultApi.md#contentTasksTaskIdAnswerDelete) | **DELETE** /content/tasks/{taskId}/answer |  |
| [**contentTasksTaskIdAnswerGet**](DefaultApi.md#contentTasksTaskIdAnswerGet) | **GET** /content/tasks/{taskId}/answer |  |
| [**contentTasksTaskIdAnswerPost**](DefaultApi.md#contentTasksTaskIdAnswerPost) | **POST** /content/tasks/{taskId}/answer |  |
| [**contentTasksTaskIdAnswerPut**](DefaultApi.md#contentTasksTaskIdAnswerPut) | **PUT** /content/tasks/{taskId}/answer |  |
| [**contentTasksTaskIdContentContentIdDelete**](DefaultApi.md#contentTasksTaskIdContentContentIdDelete) | **DELETE** /content/tasks/{taskId}/content/{contentId} |  |
| [**contentTasksTaskIdContentContentIdGet**](DefaultApi.md#contentTasksTaskIdContentContentIdGet) | **GET** /content/tasks/{taskId}/content/{contentId} |  |
| [**contentTasksTaskIdContentContentIdPut**](DefaultApi.md#contentTasksTaskIdContentContentIdPut) | **PUT** /content/tasks/{taskId}/content/{contentId} |  |
| [**contentTasksTaskIdContentPost**](DefaultApi.md#contentTasksTaskIdContentPost) | **POST** /content/tasks/{taskId}/content |  |
| [**contentTasksTaskIdDelete**](DefaultApi.md#contentTasksTaskIdDelete) | **DELETE** /content/tasks/{taskId} |  |
| [**contentTasksTaskIdGet**](DefaultApi.md#contentTasksTaskIdGet) | **GET** /content/tasks/{taskId} |  |
| [**contentTasksTaskIdOptionsOptionIdDelete**](DefaultApi.md#contentTasksTaskIdOptionsOptionIdDelete) | **DELETE** /content/tasks/{taskId}/options/{optionId} |  |
| [**contentTasksTaskIdOptionsOptionIdGet**](DefaultApi.md#contentTasksTaskIdOptionsOptionIdGet) | **GET** /content/tasks/{taskId}/options/{optionId} |  |
| [**contentTasksTaskIdOptionsOptionIdPut**](DefaultApi.md#contentTasksTaskIdOptionsOptionIdPut) | **PUT** /content/tasks/{taskId}/options/{optionId} |  |
| [**contentTasksTaskIdOptionsPost**](DefaultApi.md#contentTasksTaskIdOptionsPost) | **POST** /content/tasks/{taskId}/options |  |
| [**contentTasksTaskIdPut**](DefaultApi.md#contentTasksTaskIdPut) | **PUT** /content/tasks/{taskId} |  |
| [**contentThemesBySectionSectionIdGet**](DefaultApi.md#contentThemesBySectionSectionIdGet) | **GET** /content/themes/by-section/{sectionId} |  |
| [**contentThemesPost**](DefaultApi.md#contentThemesPost) | **POST** /content/themes |  |
| [**contentThemesThemeIdDelete**](DefaultApi.md#contentThemesThemeIdDelete) | **DELETE** /content/themes/{themeId} |  |
| [**contentThemesThemeIdGet**](DefaultApi.md#contentThemesThemeIdGet) | **GET** /content/themes/{themeId} |  |
| [**contentThemesThemeIdPut**](DefaultApi.md#contentThemesThemeIdPut) | **PUT** /content/themes/{themeId} |  |
| [**contentThemesThemeIdTasksGet**](DefaultApi.md#contentThemesThemeIdTasksGet) | **GET** /content/themes/{themeId}/tasks |  |
| [**expertStatisticsCourseCourseIdAverageProgressGet**](DefaultApi.md#expertStatisticsCourseCourseIdAverageProgressGet) | **GET** /expert/statistics/course/{courseId}/average-progress |  |
| [**expertStatisticsCourseCourseIdAverageTimeGet**](DefaultApi.md#expertStatisticsCourseCourseIdAverageTimeGet) | **GET** /expert/statistics/course/{courseId}/average-time |  |
| [**expertStatisticsCourseCourseIdStudentsCountGet**](DefaultApi.md#expertStatisticsCourseCourseIdStudentsCountGet) | **GET** /expert/statistics/course/{courseId}/students-count |  |
| [**expertStatisticsOverallAverageProgressGet**](DefaultApi.md#expertStatisticsOverallAverageProgressGet) | **GET** /expert/statistics/overall-average-progress |  |
| [**expertStatisticsOverallAverageTimeGet**](DefaultApi.md#expertStatisticsOverallAverageTimeGet) | **GET** /expert/statistics/overall-average-time |  |
| [**expertStatisticsStudentsOverallGet**](DefaultApi.md#expertStatisticsStudentsOverallGet) | **GET** /expert/statistics/students/overall |  |
| [**expertStudentsCountGet**](DefaultApi.md#expertStudentsCountGet) | **GET** /expert/students/count |  |
| [**expertStudentsGet**](DefaultApi.md#expertStudentsGet) | **GET** /expert/students |  |
| [**mediaMediaIdDelete**](DefaultApi.md#mediaMediaIdDelete) | **DELETE** /media/{mediaId} |  |
| [**mediaMediaIdGet**](DefaultApi.md#mediaMediaIdGet) | **GET** /media/{mediaId} |  |
| [**mediaMediaIdPut**](DefaultApi.md#mediaMediaIdPut) | **PUT** /media/{mediaId} |  |
| [**mediaUploadPost**](DefaultApi.md#mediaUploadPost) | **POST** /media/upload |  |
| [**profilesAvatarUserIdGet**](DefaultApi.md#profilesAvatarUserIdGet) | **GET** /profiles/avatar/{userId} |  |
| [**profilesStaffGet**](DefaultApi.md#profilesStaffGet) | **GET** /profiles/staff |  |
| [**profilesStaffIdGet**](DefaultApi.md#profilesStaffIdGet) | **GET** /profiles/staff/{id} |  |
| [**profilesStaffIdPut**](DefaultApi.md#profilesStaffIdPut) | **PUT** /profiles/staff/{id} |  |
| [**profilesStaffPost**](DefaultApi.md#profilesStaffPost) | **POST** /profiles/staff |  |
| [**profilesStaffPut**](DefaultApi.md#profilesStaffPut) | **PUT** /profiles/staff |  |
| [**profilesUserBasePut**](DefaultApi.md#profilesUserBasePut) | **PUT** /profiles/user/base |  |
| [**profilesUserGet**](DefaultApi.md#profilesUserGet) | **GET** /profiles/user |  |
| [**profilesUserIdGet**](DefaultApi.md#profilesUserIdGet) | **GET** /profiles/user/{id} |  |
| [**profilesUserIdPut**](DefaultApi.md#profilesUserIdPut) | **PUT** /profiles/user/{id} |  |
| [**profilesUserLanguageSkillsGet**](DefaultApi.md#profilesUserLanguageSkillsGet) | **GET** /profiles/user/language-skills |  |
| [**profilesUserLanguageSkillsPost**](DefaultApi.md#profilesUserLanguageSkillsPost) | **POST** /profiles/user/language-skills |  |
| [**profilesUserLanguageSkillsSkillIdDelete**](DefaultApi.md#profilesUserLanguageSkillsSkillIdDelete) | **DELETE** /profiles/user/language-skills/{skillId} |  |
| [**profilesUserLanguageSkillsSkillIdPut**](DefaultApi.md#profilesUserLanguageSkillsSkillIdPut) | **PUT** /profiles/user/language-skills/{skillId} |  |
| [**profilesUserPost**](DefaultApi.md#profilesUserPost) | **POST** /profiles/user |  |
| [**profilesUserPut**](DefaultApi.md#profilesUserPut) | **PUT** /profiles/user |  |
| [**studentBadgesGet**](DefaultApi.md#studentBadgesGet) | **GET** /student/badges |  |
| [**studentBadgesPost**](DefaultApi.md#studentBadgesPost) | **POST** /student/badges |  |
| [**studentCoursesCourseIdEnrollDelete**](DefaultApi.md#studentCoursesCourseIdEnrollDelete) | **DELETE** /student/courses/{courseId}/enroll |  |
| [**studentCoursesCourseIdEnrollPost**](DefaultApi.md#studentCoursesCourseIdEnrollPost) | **POST** /student/courses/{courseId}/enroll |  |
| [**studentCoursesCourseIdProgressGet**](DefaultApi.md#studentCoursesCourseIdProgressGet) | **GET** /student/courses/{courseId}/progress |  |
| [**studentCoursesGet**](DefaultApi.md#studentCoursesGet) | **GET** /student/courses |  |
| [**studentEnrollmentsGet**](DefaultApi.md#studentEnrollmentsGet) | **GET** /student/enrollments |  |
| [**studentSectionsSectionIdProgressGet**](DefaultApi.md#studentSectionsSectionIdProgressGet) | **GET** /student/sections/{sectionId}/progress |  |
| [**studentTaskQueueGet**](DefaultApi.md#studentTaskQueueGet) | **GET** /student/task-queue |  |
| [**studentTaskQueueNextGet**](DefaultApi.md#studentTaskQueueNextGet) | **GET** /student/task-queue/next |  |
| [**studentTaskQueuePost**](DefaultApi.md#studentTaskQueuePost) | **POST** /student/task-queue |  |
| [**studentTaskQueueQueueIdDelete**](DefaultApi.md#studentTaskQueueQueueIdDelete) | **DELETE** /student/task-queue/{queueId} |  |
| [**studentTaskQueueQueueIdPositionPatch**](DefaultApi.md#studentTaskQueueQueueIdPositionPatch) | **PATCH** /student/task-queue/{queueId}/position |  |
| [**studentTasksTaskIdAnswerGet**](DefaultApi.md#studentTasksTaskIdAnswerGet) | **GET** /student/tasks/{taskId}/answer |  |
| [**studentTasksTaskIdCompletePost**](DefaultApi.md#studentTasksTaskIdCompletePost) | **POST** /student/tasks/{taskId}/complete |  |
| [**studentTasksTaskIdProgressGet**](DefaultApi.md#studentTasksTaskIdProgressGet) | **GET** /student/tasks/{taskId}/progress |  |
| [**studentTasksTaskIdProgressPatch**](DefaultApi.md#studentTasksTaskIdProgressPatch) | **PATCH** /student/tasks/{taskId}/progress |  |
| [**studentTasksTaskIdProgressPost**](DefaultApi.md#studentTasksTaskIdProgressPost) | **POST** /student/tasks/{taskId}/progress |  |
| [**studentTasksTaskIdQueryGet**](DefaultApi.md#studentTasksTaskIdQueryGet) | **GET** /student/tasks/{taskId}/query |  |
| [**studentTasksTaskIdReportPost**](DefaultApi.md#studentTasksTaskIdReportPost) | **POST** /student/tasks/{taskId}/report |  |


<a id="adminStatisticsCourseCourseIdGet"></a>
# **adminStatisticsCourseCourseIdGet**
> OverallCourseStatisticsResponse adminStatisticsCourseCourseIdGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : OverallCourseStatisticsResponse = apiInstance.adminStatisticsCourseCourseIdGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminStatisticsCourseCourseIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminStatisticsCourseCourseIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

[**OverallCourseStatisticsResponse**](OverallCourseStatisticsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminStatisticsOverallGet"></a>
# **adminStatisticsOverallGet**
> OverallStatisticsResponse adminStatisticsOverallGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : OverallStatisticsResponse = apiInstance.adminStatisticsOverallGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminStatisticsOverallGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminStatisticsOverallGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**OverallStatisticsResponse**](OverallStatisticsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminStatisticsStudentsCourseCourseIdGet"></a>
# **adminStatisticsStudentsCourseCourseIdGet**
> kotlin.String adminStatisticsStudentsCourseCourseIdGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.adminStatisticsStudentsCourseCourseIdGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminStatisticsStudentsCourseCourseIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminStatisticsStudentsCourseCourseIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminStatisticsStudentsOverallGet"></a>
# **adminStatisticsStudentsOverallGet**
> kotlin.String adminStatisticsStudentsOverallGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.String = apiInstance.adminStatisticsStudentsOverallGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminStatisticsStudentsOverallGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminStatisticsStudentsOverallGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminUsersCountGet"></a>
# **adminUsersCountGet**
> kotlin.String adminUsersCountGet(role, createdFrom, createdTo)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val role : kotlin.String = role_example // kotlin.String | 
val createdFrom : kotlin.String = createdFrom_example // kotlin.String | 
val createdTo : kotlin.String = createdTo_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.adminUsersCountGet(role, createdFrom, createdTo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminUsersCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminUsersCountGet")
    e.printStackTrace()
}
```

### Parameters
| **role** | **kotlin.String**|  | [optional] |
| **createdFrom** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createdTo** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminUsersGet"></a>
# **adminUsersGet**
> kotlin.collections.List&lt;User&gt; adminUsersGet(page, size, role, sortBy, sortOrder, createdFrom, createdTo)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val page : kotlin.Int = 56 // kotlin.Int | 
val size : kotlin.Int = 56 // kotlin.Int | 
val role : kotlin.String = role_example // kotlin.String | 
val sortBy : kotlin.String = sortBy_example // kotlin.String | 
val sortOrder : kotlin.String = sortOrder_example // kotlin.String | 
val createdFrom : kotlin.String = createdFrom_example // kotlin.String | 
val createdTo : kotlin.String = createdTo_example // kotlin.String | 
try {
    val result : kotlin.collections.List<User> = apiInstance.adminUsersGet(page, size, role, sortBy, sortOrder, createdFrom, createdTo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminUsersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminUsersGet")
    e.printStackTrace()
}
```

### Parameters
| **page** | **kotlin.Int**|  | [optional] |
| **size** | **kotlin.Int**|  | [optional] |
| **role** | **kotlin.String**|  | [optional] |
| **sortBy** | **kotlin.String**|  | [optional] |
| **sortOrder** | **kotlin.String**|  | [optional] |
| **createdFrom** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createdTo** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminUsersStaffPost"></a>
# **adminUsersStaffPost**
> LoginResponse adminUsersStaffPost(staffRegisterRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val staffRegisterRequest : StaffRegisterRequest =  // StaffRegisterRequest | 
try {
    val result : LoginResponse = apiInstance.adminUsersStaffPost(staffRegisterRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminUsersStaffPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminUsersStaffPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **staffRegisterRequest** | [**StaffRegisterRequest**](StaffRegisterRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="adminUsersUserIdGet"></a>
# **adminUsersUserIdGet**
> User adminUsersUserIdGet(userId)



TODO(\&quot;fix authenticate for &#x60;content-jwt&#x60;\&quot;)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : User = apiInstance.adminUsersUserIdGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminUsersUserIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminUsersUserIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="adminUsersUserIdStatusPut"></a>
# **adminUsersUserIdStatusPut**
> MessageResponse adminUsersUserIdStatusPut(userId, body)



get(\&quot;/{userId}\&quot;) { &lt;br&gt; val userId &#x3D; call.parameters[\&quot;userId\&quot;] &lt;br&gt; if (userId &#x3D;&#x3D; null) { &lt;br&gt; call.respond( &lt;br&gt; HttpStatusCode.BadRequest, &lt;br&gt; ErrorResponse( &lt;br&gt; success &#x3D; false, &lt;br&gt; error &#x3D; \&quot;Missing user ID\&quot;, &lt;br&gt; code &#x3D; null, &lt;br&gt; timestamp &#x3D; System.currentTimeMillis() &lt;br&gt; ) &lt;br&gt; ) &lt;br&gt; return@get &lt;br&gt; } &lt;br&gt; val result &#x3D; adminService.getUserById(userId) &lt;br&gt; if (result.isSuccess) { &lt;br&gt; call.respond(HttpStatusCode.OK, result.getOrNull()!!) &lt;br&gt; } else { &lt;br&gt; call.respond( &lt;br&gt; HttpStatusCode.NotFound, &lt;br&gt; ErrorResponse( &lt;br&gt; success &#x3D; false, &lt;br&gt; error &#x3D; result.exceptionOrNull()?.message ?: \&quot;User not found\&quot;, &lt;br&gt; code &#x3D; null, &lt;br&gt; timestamp &#x3D; System.currentTimeMillis() &lt;br&gt; ) &lt;br&gt; ) &lt;br&gt; } &lt;br&gt; }

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val userId : kotlin.String = userId_example // kotlin.String | 
val body : kotlin.String = body_example // kotlin.String | 
try {
    val result : MessageResponse = apiInstance.adminUsersUserIdStatusPut(userId, body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#adminUsersUserIdStatusPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#adminUsersUserIdStatusPut")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.String**|  | |

### Return type

[**MessageResponse**](MessageResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="authAdminCreateInitialPost"></a>
# **authAdminCreateInitialPost**
> AdminCreatedResponse authAdminCreateInitialPost()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : AdminCreatedResponse = apiInstance.authAdminCreateInitialPost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authAdminCreateInitialPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authAdminCreateInitialPost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**AdminCreatedResponse**](AdminCreatedResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="authLoginPost"></a>
# **authLoginPost**
> LoginResponse authLoginPost(loginRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val loginRequest : LoginRequest =  // LoginRequest | 
try {
    val result : LoginResponse = apiInstance.authLoginPost(loginRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authLoginPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authLoginPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **loginRequest** | [**LoginRequest**](LoginRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="authLogoutPost"></a>
# **authLogoutPost**
> MessageResponse authLogoutPost()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : MessageResponse = apiInstance.authLogoutPost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authLogoutPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authLogoutPost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MessageResponse**](MessageResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="authMeGet"></a>
# **authMeGet**
> UserInfoResponse authMeGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : UserInfoResponse = apiInstance.authMeGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authMeGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authMeGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserInfoResponse**](UserInfoResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="authRefreshPost"></a>
# **authRefreshPost**
> kotlin.String authRefreshPost(refreshTokenRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val refreshTokenRequest : RefreshTokenRequest =  // RefreshTokenRequest | 
try {
    val result : kotlin.String = apiInstance.authRefreshPost(refreshTokenRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authRefreshPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authRefreshPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **refreshTokenRequest** | [**RefreshTokenRequest**](RefreshTokenRequest.md)|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="authStaffRegisterPost"></a>
# **authStaffRegisterPost**
> LoginResponse authStaffRegisterPost(staffRegisterRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val staffRegisterRequest : StaffRegisterRequest =  // StaffRegisterRequest | 
try {
    val result : LoginResponse = apiInstance.authStaffRegisterPost(staffRegisterRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authStaffRegisterPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authStaffRegisterPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **staffRegisterRequest** | [**StaffRegisterRequest**](StaffRegisterRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="authStudentRegisterPost"></a>
# **authStudentRegisterPost**
> LoginResponse authStudentRegisterPost(studentRegisterRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val studentRegisterRequest : StudentRegisterRequest =  // StudentRegisterRequest | 
try {
    val result : LoginResponse = apiInstance.authStudentRegisterPost(studentRegisterRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#authStudentRegisterPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#authStudentRegisterPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **studentRegisterRequest** | [**StudentRegisterRequest**](StudentRegisterRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentCoursesCourseIdDelete"></a>
# **contentCoursesCourseIdDelete**
> kotlin.String contentCoursesCourseIdDelete(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentCoursesCourseIdDelete(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentCoursesCourseIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentCoursesCourseIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentCoursesCourseIdGet"></a>
# **contentCoursesCourseIdGet**
> Course contentCoursesCourseIdGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : Course = apiInstance.contentCoursesCourseIdGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentCoursesCourseIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentCoursesCourseIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

[**Course**](Course.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentCoursesCourseIdPut"></a>
# **contentCoursesCourseIdPut**
> Course contentCoursesCourseIdPut(courseId, updateCourseRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
val updateCourseRequest : UpdateCourseRequest =  // UpdateCourseRequest | 
try {
    val result : Course = apiInstance.contentCoursesCourseIdPut(courseId, updateCourseRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentCoursesCourseIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentCoursesCourseIdPut")
    e.printStackTrace()
}
```

### Parameters
| **courseId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateCourseRequest** | [**UpdateCourseRequest**](UpdateCourseRequest.md)|  | |

### Return type

[**Course**](Course.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentCoursesGet"></a>
# **contentCoursesGet**
> kotlin.collections.List&lt;Course&gt; contentCoursesGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<Course> = apiInstance.contentCoursesGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentCoursesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentCoursesGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Course&gt;**](Course.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentCoursesPost"></a>
# **contentCoursesPost**
> Course contentCoursesPost(createCourseRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createCourseRequest : CreateCourseRequest =  // CreateCourseRequest | 
try {
    val result : Course = apiInstance.contentCoursesPost(createCourseRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentCoursesPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentCoursesPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createCourseRequest** | [**CreateCourseRequest**](CreateCourseRequest.md)|  | |

### Return type

[**Course**](Course.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentReportsGet"></a>
# **contentReportsGet**
> kotlin.collections.List&lt;Report&gt; contentReportsGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<Report> = apiInstance.contentReportsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentReportsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentReportsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Report&gt;**](Report.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentReportsPost"></a>
# **contentReportsPost**
> Report contentReportsPost(createReportRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createReportRequest : CreateReportRequest =  // CreateReportRequest | 
try {
    val result : Report = apiInstance.contentReportsPost(createReportRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentReportsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentReportsPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createReportRequest** | [**CreateReportRequest**](CreateReportRequest.md)|  | |

### Return type

[**Report**](Report.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentReportsReportIdGet"></a>
# **contentReportsReportIdGet**
> Report contentReportsReportIdGet(reportId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val reportId : kotlin.String = reportId_example // kotlin.String | 
try {
    val result : Report = apiInstance.contentReportsReportIdGet(reportId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentReportsReportIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentReportsReportIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **reportId** | **kotlin.String**|  | |

### Return type

[**Report**](Report.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentSectionsByCourseCourseIdGet"></a>
# **contentSectionsByCourseCourseIdGet**
> kotlin.collections.List&lt;Section&gt; contentSectionsByCourseCourseIdGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<Section> = apiInstance.contentSectionsByCourseCourseIdGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentSectionsByCourseCourseIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentSectionsByCourseCourseIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;Section&gt;**](Section.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentSectionsPost"></a>
# **contentSectionsPost**
> Section contentSectionsPost(createSectionRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createSectionRequest : CreateSectionRequest =  // CreateSectionRequest | 
try {
    val result : Section = apiInstance.contentSectionsPost(createSectionRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentSectionsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentSectionsPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createSectionRequest** | [**CreateSectionRequest**](CreateSectionRequest.md)|  | |

### Return type

[**Section**](Section.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentSectionsSectionIdDelete"></a>
# **contentSectionsSectionIdDelete**
> kotlin.String contentSectionsSectionIdDelete(sectionId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentSectionsSectionIdDelete(sectionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentSectionsSectionIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentSectionsSectionIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sectionId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentSectionsSectionIdGet"></a>
# **contentSectionsSectionIdGet**
> Section contentSectionsSectionIdGet(sectionId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
try {
    val result : Section = apiInstance.contentSectionsSectionIdGet(sectionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentSectionsSectionIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentSectionsSectionIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sectionId** | **kotlin.String**|  | |

### Return type

[**Section**](Section.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentSectionsSectionIdPut"></a>
# **contentSectionsSectionIdPut**
> Section contentSectionsSectionIdPut(sectionId, updateSectionRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
val updateSectionRequest : UpdateSectionRequest =  // UpdateSectionRequest | 
try {
    val result : Section = apiInstance.contentSectionsSectionIdPut(sectionId, updateSectionRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentSectionsSectionIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentSectionsSectionIdPut")
    e.printStackTrace()
}
```

### Parameters
| **sectionId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateSectionRequest** | [**UpdateSectionRequest**](UpdateSectionRequest.md)|  | |

### Return type

[**Section**](Section.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentStatsCourseCourseIdTasksCountGet"></a>
# **contentStatsCourseCourseIdTasksCountGet**
> kotlin.String contentStatsCourseCourseIdTasksCountGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentStatsCourseCourseIdTasksCountGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentStatsCourseCourseIdTasksCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentStatsCourseCourseIdTasksCountGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentStatsGet"></a>
# **contentStatsGet**
> CountStats contentStatsGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : CountStats = apiInstance.contentStatsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentStatsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentStatsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CountStats**](CountStats.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentStatsSectionSectionIdTasksCountGet"></a>
# **contentStatsSectionSectionIdTasksCountGet**
> kotlin.String contentStatsSectionSectionIdTasksCountGet(sectionId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentStatsSectionSectionIdTasksCountGet(sectionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentStatsSectionSectionIdTasksCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentStatsSectionSectionIdTasksCountGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sectionId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentStatsThemeThemeIdTasksCountGet"></a>
# **contentStatsThemeThemeIdTasksCountGet**
> kotlin.String contentStatsThemeThemeIdTasksCountGet(themeId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val themeId : kotlin.String = themeId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentStatsThemeThemeIdTasksCountGet(themeId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentStatsThemeThemeIdTasksCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentStatsThemeThemeIdTasksCountGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **themeId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksPost"></a>
# **contentTasksPost**
> TaskWithDetails contentTasksPost(createTaskRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createTaskRequest : CreateTaskRequest =  // CreateTaskRequest | 
try {
    val result : TaskWithDetails = apiInstance.contentTasksPost(createTaskRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createTaskRequest** | [**CreateTaskRequest**](CreateTaskRequest.md)|  | |

### Return type

[**TaskWithDetails**](TaskWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdAnswerDelete"></a>
# **contentTasksTaskIdAnswerDelete**
> kotlin.String contentTasksTaskIdAnswerDelete(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentTasksTaskIdAnswerDelete(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdAnswerDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdAnswerDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdAnswerGet"></a>
# **contentTasksTaskIdAnswerGet**
> TaskAnswerItem contentTasksTaskIdAnswerGet(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskAnswerItem = apiInstance.contentTasksTaskIdAnswerGet(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdAnswerGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdAnswerGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskAnswerItem**](TaskAnswerItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdAnswerPost"></a>
# **contentTasksTaskIdAnswerPost**
> TaskAnswerItem contentTasksTaskIdAnswerPost(taskId, createTaskAnswerRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val createTaskAnswerRequest : CreateTaskAnswerRequest =  // CreateTaskAnswerRequest | 
try {
    val result : TaskAnswerItem = apiInstance.contentTasksTaskIdAnswerPost(taskId, createTaskAnswerRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdAnswerPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdAnswerPost")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createTaskAnswerRequest** | [**CreateTaskAnswerRequest**](CreateTaskAnswerRequest.md)|  | |

### Return type

[**TaskAnswerItem**](TaskAnswerItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdAnswerPut"></a>
# **contentTasksTaskIdAnswerPut**
> TaskAnswerItem contentTasksTaskIdAnswerPut(taskId, updateTaskAnswerRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val updateTaskAnswerRequest : UpdateTaskAnswerRequest =  // UpdateTaskAnswerRequest | 
try {
    val result : TaskAnswerItem = apiInstance.contentTasksTaskIdAnswerPut(taskId, updateTaskAnswerRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdAnswerPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdAnswerPut")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateTaskAnswerRequest** | [**UpdateTaskAnswerRequest**](UpdateTaskAnswerRequest.md)|  | |

### Return type

[**TaskAnswerItem**](TaskAnswerItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdContentContentIdDelete"></a>
# **contentTasksTaskIdContentContentIdDelete**
> kotlin.String contentTasksTaskIdContentContentIdDelete(contentId, taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val contentId : kotlin.String = contentId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentTasksTaskIdContentContentIdDelete(contentId, taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdContentContentIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdContentContentIdDelete")
    e.printStackTrace()
}
```

### Parameters
| **contentId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdContentContentIdGet"></a>
# **contentTasksTaskIdContentContentIdGet**
> TaskContentItem contentTasksTaskIdContentContentIdGet(contentId, taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val contentId : kotlin.String = contentId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskContentItem = apiInstance.contentTasksTaskIdContentContentIdGet(contentId, taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdContentContentIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdContentContentIdGet")
    e.printStackTrace()
}
```

### Parameters
| **contentId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskContentItem**](TaskContentItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdContentContentIdPut"></a>
# **contentTasksTaskIdContentContentIdPut**
> TaskContentItem contentTasksTaskIdContentContentIdPut(contentId, taskId, updateTaskContentRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val contentId : kotlin.String = contentId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
val updateTaskContentRequest : UpdateTaskContentRequest =  // UpdateTaskContentRequest | 
try {
    val result : TaskContentItem = apiInstance.contentTasksTaskIdContentContentIdPut(contentId, taskId, updateTaskContentRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdContentContentIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdContentContentIdPut")
    e.printStackTrace()
}
```

### Parameters
| **contentId** | **kotlin.String**|  | |
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateTaskContentRequest** | [**UpdateTaskContentRequest**](UpdateTaskContentRequest.md)|  | |

### Return type

[**TaskContentItem**](TaskContentItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdContentPost"></a>
# **contentTasksTaskIdContentPost**
> TaskContentItem contentTasksTaskIdContentPost(taskId, createTaskContentRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val createTaskContentRequest : CreateTaskContentRequest =  // CreateTaskContentRequest | 
try {
    val result : TaskContentItem = apiInstance.contentTasksTaskIdContentPost(taskId, createTaskContentRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdContentPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdContentPost")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createTaskContentRequest** | [**CreateTaskContentRequest**](CreateTaskContentRequest.md)|  | |

### Return type

[**TaskContentItem**](TaskContentItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdDelete"></a>
# **contentTasksTaskIdDelete**
> kotlin.String contentTasksTaskIdDelete(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentTasksTaskIdDelete(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdGet"></a>
# **contentTasksTaskIdGet**
> TaskWithDetails contentTasksTaskIdGet(taskId)



TODO(\&quot;fix authenticate for &#x60;content-jwt&#x60;\&quot;)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskWithDetails = apiInstance.contentTasksTaskIdGet(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskWithDetails**](TaskWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdOptionsOptionIdDelete"></a>
# **contentTasksTaskIdOptionsOptionIdDelete**
> kotlin.String contentTasksTaskIdOptionsOptionIdDelete(optionId, taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val optionId : kotlin.String = optionId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentTasksTaskIdOptionsOptionIdDelete(optionId, taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdDelete")
    e.printStackTrace()
}
```

### Parameters
| **optionId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdOptionsOptionIdGet"></a>
# **contentTasksTaskIdOptionsOptionIdGet**
> TaskAnswerOptionItem contentTasksTaskIdOptionsOptionIdGet(optionId, taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val optionId : kotlin.String = optionId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskAnswerOptionItem = apiInstance.contentTasksTaskIdOptionsOptionIdGet(optionId, taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdGet")
    e.printStackTrace()
}
```

### Parameters
| **optionId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskAnswerOptionItem**](TaskAnswerOptionItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentTasksTaskIdOptionsOptionIdPut"></a>
# **contentTasksTaskIdOptionsOptionIdPut**
> TaskAnswerOptionItem contentTasksTaskIdOptionsOptionIdPut(optionId, taskId, updateTaskAnswerOptionRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val optionId : kotlin.String = optionId_example // kotlin.String | 
val taskId : kotlin.String = taskId_example // kotlin.String | 
val updateTaskAnswerOptionRequest : UpdateTaskAnswerOptionRequest =  // UpdateTaskAnswerOptionRequest | 
try {
    val result : TaskAnswerOptionItem = apiInstance.contentTasksTaskIdOptionsOptionIdPut(optionId, taskId, updateTaskAnswerOptionRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdOptionsOptionIdPut")
    e.printStackTrace()
}
```

### Parameters
| **optionId** | **kotlin.String**|  | |
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateTaskAnswerOptionRequest** | [**UpdateTaskAnswerOptionRequest**](UpdateTaskAnswerOptionRequest.md)|  | |

### Return type

[**TaskAnswerOptionItem**](TaskAnswerOptionItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdOptionsPost"></a>
# **contentTasksTaskIdOptionsPost**
> TaskAnswerOptionItem contentTasksTaskIdOptionsPost(taskId, createTaskAnswerOptionRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val createTaskAnswerOptionRequest : CreateTaskAnswerOptionRequest =  // CreateTaskAnswerOptionRequest | 
try {
    val result : TaskAnswerOptionItem = apiInstance.contentTasksTaskIdOptionsPost(taskId, createTaskAnswerOptionRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdOptionsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdOptionsPost")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createTaskAnswerOptionRequest** | [**CreateTaskAnswerOptionRequest**](CreateTaskAnswerOptionRequest.md)|  | |

### Return type

[**TaskAnswerOptionItem**](TaskAnswerOptionItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentTasksTaskIdPut"></a>
# **contentTasksTaskIdPut**
> TaskWithDetails contentTasksTaskIdPut(taskId, updateTaskRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val updateTaskRequest : UpdateTaskRequest =  // UpdateTaskRequest | 
try {
    val result : TaskWithDetails = apiInstance.contentTasksTaskIdPut(taskId, updateTaskRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentTasksTaskIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentTasksTaskIdPut")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateTaskRequest** | [**UpdateTaskRequest**](UpdateTaskRequest.md)|  | |

### Return type

[**TaskWithDetails**](TaskWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentThemesBySectionSectionIdGet"></a>
# **contentThemesBySectionSectionIdGet**
> kotlin.collections.List&lt;Theme&gt; contentThemesBySectionSectionIdGet(sectionId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<Theme> = apiInstance.contentThemesBySectionSectionIdGet(sectionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesBySectionSectionIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesBySectionSectionIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sectionId** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;Theme&gt;**](Theme.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentThemesPost"></a>
# **contentThemesPost**
> Theme contentThemesPost(createThemeRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createThemeRequest : CreateThemeRequest =  // CreateThemeRequest | 
try {
    val result : Theme = apiInstance.contentThemesPost(createThemeRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createThemeRequest** | [**CreateThemeRequest**](CreateThemeRequest.md)|  | |

### Return type

[**Theme**](Theme.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentThemesThemeIdDelete"></a>
# **contentThemesThemeIdDelete**
> kotlin.String contentThemesThemeIdDelete(themeId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val themeId : kotlin.String = themeId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.contentThemesThemeIdDelete(themeId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesThemeIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesThemeIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **themeId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentThemesThemeIdGet"></a>
# **contentThemesThemeIdGet**
> Theme contentThemesThemeIdGet(themeId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val themeId : kotlin.String = themeId_example // kotlin.String | 
try {
    val result : Theme = apiInstance.contentThemesThemeIdGet(themeId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesThemeIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesThemeIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **themeId** | **kotlin.String**|  | |

### Return type

[**Theme**](Theme.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="contentThemesThemeIdPut"></a>
# **contentThemesThemeIdPut**
> Theme contentThemesThemeIdPut(themeId, updateThemeRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val themeId : kotlin.String = themeId_example // kotlin.String | 
val updateThemeRequest : UpdateThemeRequest =  // UpdateThemeRequest | 
try {
    val result : Theme = apiInstance.contentThemesThemeIdPut(themeId, updateThemeRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesThemeIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesThemeIdPut")
    e.printStackTrace()
}
```

### Parameters
| **themeId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateThemeRequest** | [**UpdateThemeRequest**](UpdateThemeRequest.md)|  | |

### Return type

[**Theme**](Theme.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="contentThemesThemeIdTasksGet"></a>
# **contentThemesThemeIdTasksGet**
> kotlin.collections.List&lt;TaskWithDetails&gt; contentThemesThemeIdTasksGet(themeId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val themeId : kotlin.String = themeId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<TaskWithDetails> = apiInstance.contentThemesThemeIdTasksGet(themeId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#contentThemesThemeIdTasksGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#contentThemesThemeIdTasksGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **themeId** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;TaskWithDetails&gt;**](TaskWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsCourseCourseIdAverageProgressGet"></a>
# **expertStatisticsCourseCourseIdAverageProgressGet**
> kotlin.String expertStatisticsCourseCourseIdAverageProgressGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.expertStatisticsCourseCourseIdAverageProgressGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsCourseCourseIdAverageProgressGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsCourseCourseIdAverageProgressGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsCourseCourseIdAverageTimeGet"></a>
# **expertStatisticsCourseCourseIdAverageTimeGet**
> kotlin.String expertStatisticsCourseCourseIdAverageTimeGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.expertStatisticsCourseCourseIdAverageTimeGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsCourseCourseIdAverageTimeGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsCourseCourseIdAverageTimeGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsCourseCourseIdStudentsCountGet"></a>
# **expertStatisticsCourseCourseIdStudentsCountGet**
> kotlin.String expertStatisticsCourseCourseIdStudentsCountGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.expertStatisticsCourseCourseIdStudentsCountGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsCourseCourseIdStudentsCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsCourseCourseIdStudentsCountGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsOverallAverageProgressGet"></a>
# **expertStatisticsOverallAverageProgressGet**
> kotlin.String expertStatisticsOverallAverageProgressGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.String = apiInstance.expertStatisticsOverallAverageProgressGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsOverallAverageProgressGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsOverallAverageProgressGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsOverallAverageTimeGet"></a>
# **expertStatisticsOverallAverageTimeGet**
> kotlin.String expertStatisticsOverallAverageTimeGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.String = apiInstance.expertStatisticsOverallAverageTimeGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsOverallAverageTimeGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsOverallAverageTimeGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStatisticsStudentsOverallGet"></a>
# **expertStatisticsStudentsOverallGet**
> kotlin.String expertStatisticsStudentsOverallGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.String = apiInstance.expertStatisticsStudentsOverallGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStatisticsStudentsOverallGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStatisticsStudentsOverallGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStudentsCountGet"></a>
# **expertStudentsCountGet**
> kotlin.String expertStudentsCountGet(createdFrom, createdTo)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createdFrom : kotlin.String = createdFrom_example // kotlin.String | 
val createdTo : kotlin.String = createdTo_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.expertStudentsCountGet(createdFrom, createdTo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStudentsCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStudentsCountGet")
    e.printStackTrace()
}
```

### Parameters
| **createdFrom** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createdTo** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="expertStudentsGet"></a>
# **expertStudentsGet**
> kotlin.collections.List&lt;User&gt; expertStudentsGet(page, size, sortBy, sortOrder, createdFrom, createdTo)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val page : kotlin.Int = 56 // kotlin.Int | 
val size : kotlin.Int = 56 // kotlin.Int | 
val sortBy : kotlin.String = sortBy_example // kotlin.String | 
val sortOrder : kotlin.String = sortOrder_example // kotlin.String | 
val createdFrom : kotlin.String = createdFrom_example // kotlin.String | 
val createdTo : kotlin.String = createdTo_example // kotlin.String | 
try {
    val result : kotlin.collections.List<User> = apiInstance.expertStudentsGet(page, size, sortBy, sortOrder, createdFrom, createdTo)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#expertStudentsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#expertStudentsGet")
    e.printStackTrace()
}
```

### Parameters
| **page** | **kotlin.Int**|  | [optional] |
| **size** | **kotlin.Int**|  | [optional] |
| **sortBy** | **kotlin.String**|  | [optional] |
| **sortOrder** | **kotlin.String**|  | [optional] |
| **createdFrom** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createdTo** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="mediaMediaIdDelete"></a>
# **mediaMediaIdDelete**
> kotlin.String mediaMediaIdDelete(mediaId, userId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val mediaId : kotlin.String = mediaId_example // kotlin.String | 
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.mediaMediaIdDelete(mediaId, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#mediaMediaIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#mediaMediaIdDelete")
    e.printStackTrace()
}
```

### Parameters
| **mediaId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="mediaMediaIdGet"></a>
# **mediaMediaIdGet**
> kotlin.String mediaMediaIdGet(mediaId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val mediaId : kotlin.String = mediaId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.mediaMediaIdGet(mediaId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#mediaMediaIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#mediaMediaIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mediaId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*, application/*

<a id="mediaMediaIdPut"></a>
# **mediaMediaIdPut**
> MediaFileMeta mediaMediaIdPut(mediaId, userId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val mediaId : kotlin.String = mediaId_example // kotlin.String | 
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : MediaFileMeta = apiInstance.mediaMediaIdPut(mediaId, userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#mediaMediaIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#mediaMediaIdPut")
    e.printStackTrace()
}
```

### Parameters
| **mediaId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | [optional] |

### Return type

[**MediaFileMeta**](MediaFileMeta.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="mediaUploadPost"></a>
# **mediaUploadPost**
> MediaFileMeta mediaUploadPost(userId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : MediaFileMeta = apiInstance.mediaUploadPost(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#mediaUploadPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#mediaUploadPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | [optional] |

### Return type

[**MediaFileMeta**](MediaFileMeta.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesAvatarUserIdGet"></a>
# **profilesAvatarUserIdGet**
> kotlin.String profilesAvatarUserIdGet(userId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesAvatarUserIdGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesAvatarUserIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesAvatarUserIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesStaffGet"></a>
# **profilesStaffGet**
> StaffProfileResponse profilesStaffGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : StaffProfileResponse = apiInstance.profilesStaffGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesStaffGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesStaffGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**StaffProfileResponse**](StaffProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesStaffIdGet"></a>
# **profilesStaffIdGet**
> StaffProfileResponse profilesStaffIdGet(id)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : StaffProfileResponse = apiInstance.profilesStaffIdGet(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesStaffIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesStaffIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

[**StaffProfileResponse**](StaffProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesStaffIdPut"></a>
# **profilesStaffIdPut**
> StaffProfileResponse profilesStaffIdPut(id, updateStaffProfileRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
val updateStaffProfileRequest : UpdateStaffProfileRequest =  // UpdateStaffProfileRequest | 
try {
    val result : StaffProfileResponse = apiInstance.profilesStaffIdPut(id, updateStaffProfileRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesStaffIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesStaffIdPut")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateStaffProfileRequest** | [**UpdateStaffProfileRequest**](UpdateStaffProfileRequest.md)|  | |

### Return type

[**StaffProfileResponse**](StaffProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesStaffPost"></a>
# **profilesStaffPost**
> StaffProfileResponse profilesStaffPost(createStaffProfileRequest, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createStaffProfileRequest : CreateStaffProfileRequest =  // CreateStaffProfileRequest | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : StaffProfileResponse = apiInstance.profilesStaffPost(createStaffProfileRequest, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesStaffPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesStaffPost")
    e.printStackTrace()
}
```

### Parameters
| **createStaffProfileRequest** | [**CreateStaffProfileRequest**](CreateStaffProfileRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

[**StaffProfileResponse**](StaffProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesStaffPut"></a>
# **profilesStaffPut**
> StaffProfileResponse profilesStaffPut(updateStaffProfileRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val updateStaffProfileRequest : UpdateStaffProfileRequest =  // UpdateStaffProfileRequest | 
try {
    val result : StaffProfileResponse = apiInstance.profilesStaffPut(updateStaffProfileRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesStaffPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesStaffPut")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateStaffProfileRequest** | [**UpdateStaffProfileRequest**](UpdateStaffProfileRequest.md)|  | |

### Return type

[**StaffProfileResponse**](StaffProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserBasePut"></a>
# **profilesUserBasePut**
> kotlin.String profilesUserBasePut(updateUserRequest, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val updateUserRequest : UpdateUserRequest =  // UpdateUserRequest | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesUserBasePut(updateUserRequest, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserBasePut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserBasePut")
    e.printStackTrace()
}
```

### Parameters
| **updateUserRequest** | [**UpdateUserRequest**](UpdateUserRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserGet"></a>
# **profilesUserGet**
> UserProfileResponse profilesUserGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : UserProfileResponse = apiInstance.profilesUserGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserProfileResponse**](UserProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesUserIdGet"></a>
# **profilesUserIdGet**
> UserProfileResponse profilesUserIdGet(id)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : UserProfileResponse = apiInstance.profilesUserIdGet(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

[**UserProfileResponse**](UserProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesUserIdPut"></a>
# **profilesUserIdPut**
> UserProfileResponse profilesUserIdPut(id, updateUserProfileRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
val updateUserProfileRequest : UpdateUserProfileRequest =  // UpdateUserProfileRequest | 
try {
    val result : UserProfileResponse = apiInstance.profilesUserIdPut(id, updateUserProfileRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserIdPut")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateUserProfileRequest** | [**UpdateUserProfileRequest**](UpdateUserProfileRequest.md)|  | |

### Return type

[**UserProfileResponse**](UserProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserLanguageSkillsGet"></a>
# **profilesUserLanguageSkillsGet**
> kotlin.String profilesUserLanguageSkillsGet(targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesUserLanguageSkillsGet(targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserLanguageSkillsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserLanguageSkillsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesUserLanguageSkillsPost"></a>
# **profilesUserLanguageSkillsPost**
> kotlin.String profilesUserLanguageSkillsPost(userLanguageSkillRequest, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val userLanguageSkillRequest : UserLanguageSkillRequest =  // UserLanguageSkillRequest | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesUserLanguageSkillsPost(userLanguageSkillRequest, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserLanguageSkillsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserLanguageSkillsPost")
    e.printStackTrace()
}
```

### Parameters
| **userLanguageSkillRequest** | [**UserLanguageSkillRequest**](UserLanguageSkillRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserLanguageSkillsSkillIdDelete"></a>
# **profilesUserLanguageSkillsSkillIdDelete**
> kotlin.String profilesUserLanguageSkillsSkillIdDelete(skillId, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val skillId : kotlin.String = skillId_example // kotlin.String | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesUserLanguageSkillsSkillIdDelete(skillId, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserLanguageSkillsSkillIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserLanguageSkillsSkillIdDelete")
    e.printStackTrace()
}
```

### Parameters
| **skillId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="profilesUserLanguageSkillsSkillIdPut"></a>
# **profilesUserLanguageSkillsSkillIdPut**
> kotlin.String profilesUserLanguageSkillsSkillIdPut(skillId, userLanguageSkillRequest, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val skillId : kotlin.String = skillId_example // kotlin.String | 
val userLanguageSkillRequest : UserLanguageSkillRequest =  // UserLanguageSkillRequest | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : kotlin.String = apiInstance.profilesUserLanguageSkillsSkillIdPut(skillId, userLanguageSkillRequest, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserLanguageSkillsSkillIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserLanguageSkillsSkillIdPut")
    e.printStackTrace()
}
```

### Parameters
| **skillId** | **kotlin.String**|  | |
| **userLanguageSkillRequest** | [**UserLanguageSkillRequest**](UserLanguageSkillRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserPost"></a>
# **profilesUserPost**
> UserProfileResponse profilesUserPost(createUserProfileRequest, targetUserId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createUserProfileRequest : CreateUserProfileRequest =  // CreateUserProfileRequest | 
val targetUserId : kotlin.String = targetUserId_example // kotlin.String | 
try {
    val result : UserProfileResponse = apiInstance.profilesUserPost(createUserProfileRequest, targetUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserPost")
    e.printStackTrace()
}
```

### Parameters
| **createUserProfileRequest** | [**CreateUserProfileRequest**](CreateUserProfileRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **targetUserId** | **kotlin.String**|  | [optional] |

### Return type

[**UserProfileResponse**](UserProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="profilesUserPut"></a>
# **profilesUserPut**
> UserProfileResponse profilesUserPut(updateUserProfileRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val updateUserProfileRequest : UpdateUserProfileRequest =  // UpdateUserProfileRequest | 
try {
    val result : UserProfileResponse = apiInstance.profilesUserPut(updateUserProfileRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#profilesUserPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#profilesUserPut")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateUserProfileRequest** | [**UpdateUserProfileRequest**](UpdateUserProfileRequest.md)|  | |

### Return type

[**UserProfileResponse**](UserProfileResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentBadgesGet"></a>
# **studentBadgesGet**
> kotlin.collections.List&lt;Badge&gt; studentBadgesGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<Badge> = apiInstance.studentBadgesGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentBadgesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentBadgesGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Badge&gt;**](Badge.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentBadgesPost"></a>
# **studentBadgesPost**
> kotlin.Boolean studentBadgesPost(body)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val body : kotlin.String = body_example // kotlin.String | 
try {
    val result : kotlin.Boolean = apiInstance.studentBadgesPost(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentBadgesPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentBadgesPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.String**|  | |

### Return type

**kotlin.Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentCoursesCourseIdEnrollDelete"></a>
# **studentCoursesCourseIdEnrollDelete**
> kotlin.Boolean studentCoursesCourseIdEnrollDelete(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.Boolean = apiInstance.studentCoursesCourseIdEnrollDelete(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentCoursesCourseIdEnrollDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentCoursesCourseIdEnrollDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentCoursesCourseIdEnrollPost"></a>
# **studentCoursesCourseIdEnrollPost**
> kotlin.Boolean studentCoursesCourseIdEnrollPost(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : kotlin.Boolean = apiInstance.studentCoursesCourseIdEnrollPost(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentCoursesCourseIdEnrollPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentCoursesCourseIdEnrollPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

**kotlin.Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentCoursesCourseIdProgressGet"></a>
# **studentCoursesCourseIdProgressGet**
> CourseProgressItem studentCoursesCourseIdProgressGet(courseId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val courseId : kotlin.String = courseId_example // kotlin.String | 
try {
    val result : CourseProgressItem = apiInstance.studentCoursesCourseIdProgressGet(courseId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentCoursesCourseIdProgressGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentCoursesCourseIdProgressGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **courseId** | **kotlin.String**|  | |

### Return type

[**CourseProgressItem**](CourseProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentCoursesGet"></a>
# **studentCoursesGet**
> kotlin.collections.List&lt;Course&gt; studentCoursesGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<Course> = apiInstance.studentCoursesGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentCoursesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentCoursesGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Course&gt;**](Course.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentEnrollmentsGet"></a>
# **studentEnrollmentsGet**
> kotlin.collections.List&lt;UserCourseEnrollmentItem&gt; studentEnrollmentsGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<UserCourseEnrollmentItem> = apiInstance.studentEnrollmentsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentEnrollmentsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentEnrollmentsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;UserCourseEnrollmentItem&gt;**](UserCourseEnrollmentItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentSectionsSectionIdProgressGet"></a>
# **studentSectionsSectionIdProgressGet**
> SectionProgressItem studentSectionsSectionIdProgressGet(sectionId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val sectionId : kotlin.String = sectionId_example // kotlin.String | 
try {
    val result : SectionProgressItem = apiInstance.studentSectionsSectionIdProgressGet(sectionId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentSectionsSectionIdProgressGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentSectionsSectionIdProgressGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sectionId** | **kotlin.String**|  | |

### Return type

[**SectionProgressItem**](SectionProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTaskQueueGet"></a>
# **studentTaskQueueGet**
> kotlin.collections.List&lt;UserTaskQueueItem&gt; studentTaskQueueGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<UserTaskQueueItem> = apiInstance.studentTaskQueueGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTaskQueueGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTaskQueueGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;UserTaskQueueItem&gt;**](UserTaskQueueItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTaskQueueNextGet"></a>
# **studentTaskQueueNextGet**
> UserTaskQueueItem studentTaskQueueNextGet()





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
try {
    val result : UserTaskQueueItem = apiInstance.studentTaskQueueNextGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTaskQueueNextGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTaskQueueNextGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserTaskQueueItem**](UserTaskQueueItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTaskQueuePost"></a>
# **studentTaskQueuePost**
> UserTaskQueueItem studentTaskQueuePost(createTaskQueueRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val createTaskQueueRequest : CreateTaskQueueRequest =  // CreateTaskQueueRequest | 
try {
    val result : UserTaskQueueItem = apiInstance.studentTaskQueuePost(createTaskQueueRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTaskQueuePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTaskQueuePost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createTaskQueueRequest** | [**CreateTaskQueueRequest**](CreateTaskQueueRequest.md)|  | |

### Return type

[**UserTaskQueueItem**](UserTaskQueueItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentTaskQueueQueueIdDelete"></a>
# **studentTaskQueueQueueIdDelete**
> kotlin.Boolean studentTaskQueueQueueIdDelete(queueId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val queueId : kotlin.String = queueId_example // kotlin.String | 
try {
    val result : kotlin.Boolean = apiInstance.studentTaskQueueQueueIdDelete(queueId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTaskQueueQueueIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTaskQueueQueueIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **queueId** | **kotlin.String**|  | |

### Return type

**kotlin.Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTaskQueueQueueIdPositionPatch"></a>
# **studentTaskQueueQueueIdPositionPatch**
> kotlin.Boolean studentTaskQueueQueueIdPositionPatch(queueId, body)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val queueId : kotlin.String = queueId_example // kotlin.String | 
val body : kotlin.String = body_example // kotlin.String | 
try {
    val result : kotlin.Boolean = apiInstance.studentTaskQueueQueueIdPositionPatch(queueId, body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTaskQueueQueueIdPositionPatch")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTaskQueueQueueIdPositionPatch")
    e.printStackTrace()
}
```

### Parameters
| **queueId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.String**|  | |

### Return type

**kotlin.Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentTasksTaskIdAnswerGet"></a>
# **studentTasksTaskIdAnswerGet**
> TaskAnswerItem studentTasksTaskIdAnswerGet(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskAnswerItem = apiInstance.studentTasksTaskIdAnswerGet(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdAnswerGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdAnswerGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskAnswerItem**](TaskAnswerItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTasksTaskIdCompletePost"></a>
# **studentTasksTaskIdCompletePost**
> UserTaskProgressItem studentTasksTaskIdCompletePost(taskId, body)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val body : kotlin.String = body_example // kotlin.String | 
try {
    val result : UserTaskProgressItem = apiInstance.studentTasksTaskIdCompletePost(taskId, body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdCompletePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdCompletePost")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.String**|  | |

### Return type

[**UserTaskProgressItem**](UserTaskProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentTasksTaskIdProgressGet"></a>
# **studentTasksTaskIdProgressGet**
> UserTaskProgressItem studentTasksTaskIdProgressGet(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : UserTaskProgressItem = apiInstance.studentTasksTaskIdProgressGet(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdProgressGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdProgressGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**UserTaskProgressItem**](UserTaskProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTasksTaskIdProgressPatch"></a>
# **studentTasksTaskIdProgressPatch**
> UserTaskProgressItem studentTasksTaskIdProgressPatch(taskId, updateTaskProgressRequest)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val updateTaskProgressRequest : UpdateTaskProgressRequest =  // UpdateTaskProgressRequest | 
try {
    val result : UserTaskProgressItem = apiInstance.studentTasksTaskIdProgressPatch(taskId, updateTaskProgressRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdProgressPatch")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdProgressPatch")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateTaskProgressRequest** | [**UpdateTaskProgressRequest**](UpdateTaskProgressRequest.md)|  | |

### Return type

[**UserTaskProgressItem**](UserTaskProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a id="studentTasksTaskIdProgressPost"></a>
# **studentTasksTaskIdProgressPost**
> UserTaskProgressItem studentTasksTaskIdProgressPost(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : UserTaskProgressItem = apiInstance.studentTasksTaskIdProgressPost(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdProgressPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdProgressPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**UserTaskProgressItem**](UserTaskProgressItem.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTasksTaskIdQueryGet"></a>
# **studentTasksTaskIdQueryGet**
> TaskWithDetails studentTasksTaskIdQueryGet(taskId)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
try {
    val result : TaskWithDetails = apiInstance.studentTasksTaskIdQueryGet(taskId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdQueryGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdQueryGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **taskId** | **kotlin.String**|  | |

### Return type

[**TaskWithDetails**](TaskWithDetails.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a id="studentTasksTaskIdReportPost"></a>
# **studentTasksTaskIdReportPost**
> Report studentTasksTaskIdReportPost(taskId, body)





### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = DefaultApi()
val taskId : kotlin.String = taskId_example // kotlin.String | 
val body : kotlin.String = body_example // kotlin.String | 
try {
    val result : Report = apiInstance.studentTasksTaskIdReportPost(taskId, body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#studentTasksTaskIdReportPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#studentTasksTaskIdReportPost")
    e.printStackTrace()
}
```

### Parameters
| **taskId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.String**|  | |

### Return type

[**Report**](Report.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

