using CentralPeerCoordinator.Contracts.Entities;
using CentralPeerCoordinator.Contracts.Repositories;
using CentralPeerCoordinator.Contracts.UoW;
using CentralPeerCoordinator.Data.Db.Context;
using CentralPeerCoordinator.Data.Db.Repositories;

namespace CentralPeerCoordinator.Data.Db.UoW;

public class UnitOfWork : IUnitOfWork
{
    private readonly IRepository<Peer>? _peers;

    private readonly ApplicationDbContext _dbContext;

    public UnitOfWork(ApplicationDbContext context)
    {
        _dbContext = context;
    }

    public IRepository<Peer> Peers
        => _peers ?? new Repository<Peer>(_dbContext);

    public async Task SaveChangesAsync()
    {
        await _dbContext.SaveChangesAsync();
    }
}

