#version 140

in vec2 position;

out vec2 pass_TextureCoords;
out int vertexX;
out int vertexY;

uniform mat4 projectionMatrix;
uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position.x, position.y, -10.0, 1.0);
	pass_TextureCoords = position;
	vertexX = position.x == 1 ? 1 : 0;
	vertexY = position.y == 1 ? 1 : 0;
}
