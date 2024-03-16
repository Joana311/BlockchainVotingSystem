using CentralPeerCoordinator.Contracts.UoW;
using CentralPeerCoordinator.Data.Db.Context;
using CentralPeerCoordinator.Data.Db.UoW;
using Microsoft.AspNetCore.Diagnostics;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddDbContext<ApplicationDbContext>(opt =>
{
    opt.UseSqlServer(builder.Configuration.GetConnectionString("CentralPeerCoordinatorDb"),
        opt => opt.MigrationsAssembly("CentralPeerCoordinator.Data.Db"));
});

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddScoped<IUnitOfWork, UnitOfWork>();


var app = builder.Build();


if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}


app.UseHttpsRedirection();

app.UseAuthorization();


app.UseMiddleware<ExceptionHandlerMiddleware>();

app.MapControllers();

app.Run();

