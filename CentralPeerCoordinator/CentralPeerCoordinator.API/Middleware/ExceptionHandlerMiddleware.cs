using CentralPeerCoordinator.Contracts.Exceptions;
using System.Net;
using System.Text.Json;

namespace CentralPeerCoordinator.API.Middleware;

public class ExceptionHandlerMiddleware
{
    private readonly RequestDelegate _next;

    public ExceptionHandlerMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task Invoke(HttpContext context)
    {
        try
        {
            await _next(context);
        }
        catch (Exception ex)
        {
            await OnException(context, ex);
        }
    }

    public async Task OnException(HttpContext context, Exception ex)
    {
        object response;
        _ = ex switch
        {
            EntityNotFoundException =>
                context.Response.StatusCode = (int)HttpStatusCode.NotFound,
            IpAddressNotUniqueException =>
                context.Response.StatusCode = (int)HttpStatusCode.BadRequest,
            _ => context.Response.StatusCode = (int)HttpStatusCode.InternalServerError
        };

        response = new
        {
            ex.Message
        };

        context.Response.ContentType = "application/json";

        var resultJson = JsonSerializer.Serialize(response);
        await context.Response.WriteAsync(resultJson);
    }
}


