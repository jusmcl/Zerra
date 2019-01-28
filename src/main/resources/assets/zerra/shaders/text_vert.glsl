#version 140

in vec2 position;
in vec2 texcoords;

out vec2 pass_TextureCoords;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;

void main() {
vec4 worldPosition = transformationMatrix * vec4(position.x, position.y, -10.0, 1.0);
	gl_Position = projectionMatrix * worldPosition;
	pass_TextureCoords = texcoords;
}
