namespace CentralPeerCoordinator.Contracts.Dtos;

public class PeerResponseDto
{
    public Guid Id { get; set; }

    public string IpAddress { get; set; } = default!;

    public int Port { get; set; }
}
