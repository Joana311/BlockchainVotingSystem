using CentralPeerCoordinator.API.Middleware;
using CentralPeerCoordinator.API.Validators;
using CentralPeerCoordinator.Common.Profiles;
using CentralPeerCoordinator.Contracts.Services;
using CentralPeerCoordinator.Contracts.UoW;
using CentralPeerCoordinator.Data.Db.Context;
using CentralPeerCoordinator.Data.Db.UoW;
using CentralPeerCoordinator.Services;
using FluentValidation.AspNetCore;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddDbContext<ApplicationDbContext>(opt =>
{
    opt.UseSqlServer(builder.Configuration.GetConnectionString("CentralPeerCoordinatorDb"),
        opt => opt.MigrationsAssembly("CentralPeerCoordinator.Data.Db"));
});

builder.Services.AddControllers();

builder.Services
    .AddControllers(options => options.SuppressImplicitRequiredAttributeForNonNullableReferenceTypes = true)
    .AddFluentValidation(fv => fv.RegisterValidatorsFromAssemblyContaining<PeerValidator>());

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddAutoMapper(cfg =>
{
    cfg.AddProfile<MappingProfile>();
}, AppDomain.CurrentDomain.GetAssemblies());

builder.Services.AddScoped<IUnitOfWork, UnitOfWork>();
builder.Services.AddScoped<IPeerService, PeerService>();


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

