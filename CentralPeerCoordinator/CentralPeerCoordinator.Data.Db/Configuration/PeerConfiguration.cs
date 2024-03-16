using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;

namespace CentralPeerCoordinator.Data.Db.Configuration;

public class PeerConfiguration : IEntityTypeConfiguration<Peer>
{
    public void Configure(EntityTypeBuilder<Peer> builder)
    {
        builder.ToTable("Professionals");

        builder.HasKey(p => p.Id);

        builder
            .OwnsOne(p => p.Address);

        builder
            .OwnsOne(p => p.ContactPerson);

        builder
            .HasMany(p => p.Audiences)
            .WithOne(p => p.Professional);
    }
}

