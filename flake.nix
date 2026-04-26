# NixOS runclient fix
{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = { self, nixpkgs }: let
    system = "x86_64-linux";
    pkgs = nixpkgs.legacyPackages.${system};
  in {
    devShells.${system}.default = pkgs.mkShell {
      packages = [ pkgs.jdk21 ];

      shellHook = ''
        export LD_LIBRARY_PATH=${pkgs.lib.makeLibraryPath [
          pkgs.libGL
          pkgs.libGLU
          pkgs.mesa
          pkgs.libX11
          pkgs.libXrandr
          pkgs.libXcursor
          pkgs.libXi
          pkgs.libXext
          pkgs.libXxf86vm
        ]}:$LD_LIBRARY_PATH
      '';
    };
  };
}