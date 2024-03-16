using CentralPeerCoordinator.Contracts.Dtos;
using FluentValidation;

namespace CentralPeerCoordinator.API.Validators;

public class PeerValidator : AbstractValidator<PeerRequestDto>
{
    public PeerValidator()
    {
        RuleFor(x => x.IpAddress)
            .NotNull().WithMessage("IP address is required.");

        RuleFor(x => x.Port)
            .NotNull().WithMessage("Port is required.");
    }
}

