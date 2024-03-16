using CentralPeerCoordinator.Contracts.Entities;
using Microsoft.EntityFrameworkCore;
using System.Reflection;

namespace CentralPeerCoordinator.Data.Db.Context;

public class ApplicationDbContext : DbContext
{
    public DbSet<Peer> Peers { get; set; } = default!;

    public ApplicationDbContext()
    {
    }

    public ApplicationDbContext(DbContextOptions options)
        : base(options)
    {
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
    }
}
